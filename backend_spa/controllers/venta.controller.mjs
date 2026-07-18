import * as service from "../services/venta.service.mjs";
import { ok, created } from "../utils/response.mjs";

export const listarVentas = async () => {
  const lista = await service.listado();
  return ok(lista);
};

export const registrarVenta = async (body) => {
  const nuevaVenta = await service.registrar(body);
  return created(nuevaVenta);
};
