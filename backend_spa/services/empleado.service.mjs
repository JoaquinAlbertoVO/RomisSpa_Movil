import * as repo from "../repositories/empleado.repository.mjs";
import { v4 as uuidv4 } from "uuid";

export const listado = async () => {
  const result = await repo.getEmpleados();
  return result.Items || [];
};

export const insertar = async (data) => {
  const item = {
    ...data,
    id: data.id || uuidv4()
  };
  await repo.insertarEmpleado(item);
  return item;
};

export const getById = async (id) => {
  const result = await repo.getEmpleadoById(id);
  return result.Item || null;
};

export const actualizar = async (id, data) => {
  const updateExpression = "SET #nom = :nom, #esp = :esp, #tel = :tel, #com = :com";
  const names = {
    "#nom": "nombre",
    "#esp": "especialidad",
    "#tel": "telefono",
    "#com": "comision"
  };
  const values = {
    ":nom": data.nombre,
    ":esp": data.especialidad,
    ":tel": data.telefono,
    ":com": data.comision
  };

  await repo.actualizarEmpleado(id, updateExpression, names, values);
  return { id, ...data };
};

export const eliminar = async (id) => {
  await repo.eliminarEmpleado(id);
};
