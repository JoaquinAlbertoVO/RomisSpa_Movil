import * as service from "../services/servicio.service.mjs";
import { servicioSchema } from "../validators/servicio.schema.mjs";
import { ok, created, noContent, badRequest, notFound } from "../utils/response.mjs";

export const insertarServicio = async (event) => {
  const body = JSON.parse(event.body);
  const parsed = servicioSchema.safeParse(body);

  if (!parsed.success) return badRequest(parsed.error);

  const servicio = await service.insertar(parsed.data);
  return created(servicio);
};
export const buscarServicio = async (nombre) => {
  // CORREGIDO: decodeURIComponent quita los %20 y los vuelve a hacer espacios normales
  const nombreLimpio = decodeURIComponent(nombre);
  
  const servicio = await service.getById(nombreLimpio);
  if (!servicio) return notFound("Servicio no encontrado");
  return ok(servicio);
};
export const actualizarServicio = async (nombre, event) => {
  // 1. Extraer el body de forma segura (ya sea que venga como string o como objeto)
  let body = {};
  if (event.body) {
    body = typeof event.body === "string" ? JSON.parse(event.body) : event.body;
  }

  // 2. Validar los datos con tu esquema de Zod
  const parsed = servicioSchema.safeParse(body);
  if (!parsed.success) return badRequest(parsed.error);

  // 3. Enviar los datos limpios al servicio de DynamoDB
  const servicio = await service.actualizar(nombre, parsed.data);
  return ok(servicio);
};
  
export const eliminarServicio = async (nombre) => {
  await service.eliminar(nombre);
  return noContent();
};

export const cargarServicios = async (query) => {
  const limit = query?.limit ? parseInt(query.limit) : 10;
  const result = await service.listado(limit, query?.lastKey);
  return ok(result);
};