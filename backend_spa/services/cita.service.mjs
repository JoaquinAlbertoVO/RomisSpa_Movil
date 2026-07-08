import * as repo from "../repositories/cita.repository.mjs";

export const insertar = async (data) => {
  const id = Math.random().toString(36).substring(2, 11).toUpperCase(); // ID único para Android
  const nuevaCita = { id, ...data };
  await repo.insertarCita(nuevaCita);
  return nuevaCita;
};

export const listado = async () => {
  const result = await repo.getCitas();
  return result.Items || []; // Lista limpia directa [...]
};

export const eliminar = async (id) => {
  await repo.eliminarCita(id);
};