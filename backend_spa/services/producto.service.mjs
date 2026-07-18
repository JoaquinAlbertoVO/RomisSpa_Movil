import * as repo from "../repositories/producto.repository.mjs";
import { v4 as uuidv4 } from "uuid";

export const listado = async () => {
  const result = await repo.getProductos();
  return result.Items || [];
};

export const insertar = async (data) => {
  const item = {
    ...data,
    id: data.id || uuidv4()
  };
  await repo.insertarProducto(item);
  return item;
};

export const getById = async (id) => {
  const result = await repo.getProductoById(id);
  return result.Item || null;
};

export const actualizar = async (id, data) => {
  const updateExpression = "SET #nom = :nom, #des = :des, #pre = :pre, #sto = :sto, #cat = :cat";
  const names = {
    "#nom": "nombre",
    "#des": "descripcion",
    "#pre": "precio",
    "#sto": "stock",
    "#cat": "categoria"
  };
  const values = {
    ":nom": data.nombre,
    ":des": data.descripcion,
    ":pre": data.precio,
    ":sto": data.stock,
    ":cat": data.categoria
  };

  await repo.actualizarProducto(id, updateExpression, names, values);
  return { id, ...data };
};

export const eliminar = async (id) => {
  await repo.eliminarProducto(id);
};
