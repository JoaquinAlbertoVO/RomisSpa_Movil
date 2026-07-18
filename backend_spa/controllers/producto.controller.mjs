import * as service from "../services/producto.service.mjs";
import { productoSchema } from "../validators/producto.schema.mjs";
import { ok, created, noContent, badRequest, notFound } from "../utils/response.mjs";

export const insertarProducto = async (event) => {
  let body = {};
  if (event.body) {
    body = typeof event.body === "string" ? JSON.parse(event.body) : event.body;
  }
  const parsed = productoSchema.safeParse(body);

  if (!parsed.success) return badRequest(parsed.error);

  const producto = await service.insertar(parsed.data);
  return created(producto);
};

export const buscarProducto = async (id) => {
  const producto = await service.getById(id);
  if (!producto) return notFound("Producto no encontrado");
  return ok(producto);
};

export const actualizarProducto = async (id, event) => {
  let body = {};
  if (event.body) {
    body = typeof event.body === "string" ? JSON.parse(event.body) : event.body;
  }

  const parsed = productoSchema.safeParse(body);
  if (!parsed.success) return badRequest(parsed.error);

  const producto = await service.actualizar(id, parsed.data);
  return ok(producto);
};

export const eliminarProducto = async (id) => {
  await service.eliminar(id);
  return noContent();
};

export const listarProductos = async (query) => {
  const limit = query?.limit ? parseInt(query.limit) : 50;
  const result = await service.listado(limit, query?.lastKey);
  return ok(result);
};
