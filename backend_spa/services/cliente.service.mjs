import * as repo from "../repositories/cliente.repository.mjs";

// 1. Insertar mantiene los datos limpios que vienen de tu validador Zod
export const insertar = async (data) => {
  await repo.insertarCliente(data);
  return data;
};

// 2. Listado llama correctamente a getClientes para traer la información
export const listado = async (limit = 10, lastKey) => {
  // Llamamos a getClientes pasándole los parámetros de paginación
  const result = await repo.getClientes(limit, lastKey);

  return result.Items || [];
};