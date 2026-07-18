import * as service from "../services/cita.service.mjs";
import { citaSchema } from "../validators/cita.schema.mjs";
// Importamos las funciones exactas de tu archivo response.mjs
import { ok, created, noContent, badRequest } from "../utils/response.mjs"; 

export const listarCitas = async () => {
  try {
    const lista = await service.listado();
    return ok(lista); // Usa tu función 'ok' (200)
  } catch (error) {
    // Si tuvieras un manejador genérico, si no, puedes lanzar o usar internalError
    throw error;
  }
};

export const crearCita = async (body) => {
  const validacion = citaSchema.safeParse(body);
  if (!validacion.success) {
    //  Usa tu función 'badRequest' pasándole el error directo para que lo formatee con Zod
    return badRequest(validacion.error); 
  }
  
  const nuevaCita = await service.insertar(body);
  return created(nuevaCita); // Usa tu función 'created' (201)
};

export const eliminarCita = async (id) => {
  await service.eliminar(id);
  return noContent(); // Usa tu función 'noContent' (204)
};

export const actualizarEstadoCita = async (id, body) => {
  const { estado } = body;
  if (!estado) {
    return badRequest({ message: "El estado es obligatorio" });
  }
  const actualizado = await service.actualizarEstado(id, estado);
  return ok(actualizado);
};
