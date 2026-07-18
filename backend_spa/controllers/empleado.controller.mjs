import * as service from "../services/empleado.service.mjs";
import { empleadoSchema } from "../validators/empleado.schema.mjs";
import { ok, created, noContent, badRequest, notFound } from "../utils/response.mjs";

export const insertarEmpleado = async (event) => {
  let body = {};
  if (event.body) {
    body = typeof event.body === "string" ? JSON.parse(event.body) : event.body;
  }
  const parsed = empleadoSchema.safeParse(body);

  if (!parsed.success) return badRequest(parsed.error);

  const empleado = await service.insertar(parsed.data);
  return created(empleado);
};

export const buscarEmpleado = async (id) => {
  const empleado = await service.getById(id);
  if (!empleado) return notFound("Empleado no encontrado");
  return ok(empleado);
};

export const actualizarEmpleado = async (id, event) => {
  let body = {};
  if (event.body) {
    body = typeof event.body === "string" ? JSON.parse(event.body) : event.body;
  }

  const parsed = empleadoSchema.safeParse(body);
  if (!parsed.success) return badRequest(parsed.error);

  const empleado = await service.actualizar(id, parsed.data);
  return ok(empleado);
};

export const eliminarEmpleado = async (id) => {
  await service.eliminar(id);
  return noContent();
};

export const listarEmpleados = async (query) => {
  const limit = query?.limit ? parseInt(query.limit) : 50;
  const result = await service.listado(limit, query?.lastKey);
  return ok(result);
};
