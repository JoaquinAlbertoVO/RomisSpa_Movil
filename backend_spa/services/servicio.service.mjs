import * as repo from "../repositories/servicio.repository.mjs";

// El controlador busca "service.listado"
export const listado = async () => {
    const result = await repo.getServicios();
    return result.Items || [];
};

export const insertar = async (data) => {
    await repo.insertarServicio(data);
    return data;
};

export const getById = async (nombre) => {
    const result = await repo.getServicioByNombre(nombre);
    return result.Item || null;
}

// CORREGIDO: Se exporta como "actualizar" y recibe directamente "data" limpia
export const actualizar = async (nombre, data) => {
    // 1. La instrucción que le dice a DynamoDB qué campos cambiar
    const updateExpression = "SET #desc = :desc, #prec = :prec, #img = :img";
    
    // 2. Protegemos los nombres de las columnas en tu tabla
    const names = {
        "#desc": "descripcion",
        "#prec": "precio",
        "#img": "imagenRes"
    };
    
    // 3. Asignamos los valores que vienen desde el JSON de Postman
    const values = {
        ":desc": data.descripcion,
        ":prec": data.precio,
        ":img": data.imagenRes
    };

    // 4. Invocamos tus 4 parámetros del repositorio original sin cambiarle nada
    await repo.actualizarServicio(nombre, updateExpression, names, values);
    
    return { nombre, ...data };
};

export const eliminar = async (nombre) => {
    await repo.eliminarServicio(nombre);
};