import * as service from "../services/cliente.service.mjs";
import { clienteSchema } from "../validators/cliente.schema.mjs";
import { ok, created, noContent, badRequest, notFound } from "../utils/response.mjs";

export const guardarCliente = async (event) => {
  const body = JSON.parse(event.body);
  const parsed = clienteSchema.safeParse(body);

  if (!parsed.success) return badRequest(parsed.error);

  const cliente = await service.insertar(parsed.data);
  return created(cliente);
};

export const listarCliente = async (query) => {
  const limit = query?.limit ? parseInt(query.limit) : 10;
  const result = await service.listado(limit, query?.lastKey);
  return ok(result);
};
