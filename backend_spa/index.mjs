import * as servicioController from "./controllers/servicio.controller.mjs";
import * as citaController from "./controllers/cita.controller.mjs";
//  ¡FALTABA ESTA IMPORTACIÓN CRUCIAL!
import * as clienteController from "./controllers/cliente.controller.mjs";

export const handler = async (event) => {
  try {
    // 1. Extracción de método y ruta limpia
    const method = event.httpMethod || event.requestContext?.http?.method || "";
    const path = event.path || event.requestContext?.http?.path || "";
    
    // Extraemos parámetros por si los necesitas
    const nombre = event.pathParameters?.nombre || null;
    const id = event.pathParameters?.id || null;

    // Normalizamos el body por si viene en formato string
    let body = {};
    if (event.body) {
      body = typeof event.body === "string" ? JSON.parse(event.body) : event.body;
    }

    // ==========================================
    // ENRUTADOR INTELIGENTE: CONTROL DE CLIENTES
    // ==========================================
    if (path.includes("/clientes")) {
      if (method === "GET") {
        return await clienteController.listarCliente(event.queryStringParameters);
      }
      if (method === "POST") {
        return await clienteController.guardarCliente(event);
      }
    }

    // ==========================================
    // ENRUTADOR INTELIGENTE: CONTROL DE CITAS
    // ==========================================
    if (body.cliente || path.includes("/citas")) {
      if (method === "GET") {
        return await citaController.listarCitas();
      }
      if (method === "POST") {
        return await citaController.crearCita(body);
      }
      if (method === "DELETE") {
        const idCita = id || path.split("/")[2];
        return await citaController.eliminarCita(idCita);
      }
    }

    // ==========================================
    // ENRUTADOR INTELIGENTE: CONTROL DE SERVICIOS
    // ==========================================
    // Ajustado para priorizar la ruta exacta y evitar que intercepte peticiones de clientes
    if (path.includes("/servicios") || (!path.includes("/clientes") && (body.nombre || method === "GET"))) {
      if (method === "GET") {
        if (!nombre) {
          return await servicioController.cargarServicios(event.queryStringParameters);
        } else {
          const nombreServicio = nombre || path.split("/")[2];
          return await servicioController.buscarServicio(nombreServicio);
        }
      }
      if (method === "POST") {
        return await servicioController.insertarServicio(event);
      }
      if (method === "PUT") {
        const nombreServicio = nombre || path.split("/")[2] || body.nombre;
        return await servicioController.actualizarServicio(nombreServicio, event);
      }
      if (method === "DELETE") {
        const nombreServicio = nombre || path.split("/")[2];
        return await servicioController.eliminarServicio(nombreServicio);
      }
    }

    // Si por alguna razón extraña no entra a ninguno
    return {
      statusCode: 404,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ message: `Ruta o datos no reconocidos: ${method} ${path}` })
    };

  } catch (error) {
    console.error("Unexpected Error:", error);
    return {
      statusCode: 500,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ message: "Internal Server Error", details: error.message })
    };
  }
};