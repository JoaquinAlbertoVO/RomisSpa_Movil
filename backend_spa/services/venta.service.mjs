import * as repo from "../repositories/venta.repository.mjs";

export const registrar = async (data) => {
  const id = Math.random().toString(36).substring(2, 11).toUpperCase();
  const fecha = new Date().toISOString().split('T')[0]; // YYYY-MM-DD
  const nuevaVenta = { id, fecha, ...data };
  await repo.insertarVenta(nuevaVenta);
  return nuevaVenta;
};

export const listado = async () => {
  const result = await repo.getVentas();
  return result.Items || [];
};
