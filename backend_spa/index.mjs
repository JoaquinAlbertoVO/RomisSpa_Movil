import * as servicioController from "./controllers/servicio.controller.mjs";
import * as citaController from "./controllers/cita.controller.mjs";
import * as clienteController from "./controllers/cliente.controller.mjs";
import * as ventaController from "./controllers/venta.controller.mjs";
import * as empleadoController from "./controllers/empleado.controller.mjs";
import * as productoController from "./controllers/producto.controller.mjs";

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
    // ENRUTADOR: CLIENTES
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
    // ENRUTADOR: CITAS
    // ==========================================
    if (path.includes("/citas")) {
      if (method === "GET") {
        return await citaController.listarCitas();
      }
      if (method === "POST") {
        return await citaController.crearCita(body);
      }
      if (method === "DELETE") {
        const idCita = id || path.split("/").pop();
        return await citaController.eliminarCita(idCita);
      }
      if (method === "PATCH") {
        const idCita = id || path.split("/").pop();
        return await citaController.actualizarEstadoCita(idCita, body);
      }
    }

    // ==========================================
    // ENRUTADOR: VENTAS
    // ==========================================
    if (path.includes("/ventas")) {
      if (method === "GET") {
        return await ventaController.listarVentas();
      }
      if (method === "POST") {
        return await ventaController.registrarVenta(body);
      }
    }

    // ==========================================
    // ENRUTADOR: EMPLEADOS
    // ==========================================
    if (path.includes("/empleados")) {
      if (method === "GET") {
        const empId = id || path.split("/")[2];
        if (empId && empId !== "empleados") {
          return await empleadoController.buscarEmpleado(empId);
        }
        return await empleadoController.listarEmpleados(event.queryStringParameters);
      }
      if (method === "POST") {
        return await empleadoController.insertarEmpleado(event);
      }
      if (method === "PUT") {
        const empId = id || path.split("/")[2] || body.id;
        return await empleadoController.actualizarEmpleado(empId, event);
      }
      if (method === "DELETE") {
        const empId = id || path.split("/")[2];
        return await empleadoController.eliminarEmpleado(empId);
      }
    }

    // ==========================================
    // ENRUTADOR: PRODUCTOS
    // ==========================================
    if (path.includes("/productos")) {
      if (method === "GET") {
        const prodId = id || path.split("/")[2];
        if (prodId && prodId !== "productos") {
          return await productoController.buscarProducto(prodId);
        }
        return await productoController.listarProductos(event.queryStringParameters);
      }
      if (method === "POST") {
        return await productoController.insertarProducto(event);
      }
      if (method === "PUT") {
        const prodId = id || path.split("/")[2] || body.id;
        return await productoController.actualizarProducto(prodId, event);
      }
      if (method === "DELETE") {
        const prodId = id || path.split("/")[2];
        return await productoController.eliminarProducto(prodId);
      }
    }

    // ==========================================
    // ENRUTADOR: SERVICIOS
    // ==========================================
    if (path.includes("/servicios")) {
      if (method === "GET") {
        const nombreServicio = nombre || path.split("/")[2];
        if (nombreServicio && nombreServicio !== "servicios") {
          return await servicioController.buscarServicio(nombreServicio);
        }
        return await servicioController.cargarServicios(event.queryStringParameters);
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

  } catch (error) {
    console.error("Unexpected Error:", error);
    return {
      statusCode: 500,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ message: "Internal Server Error", details: error.message })
    };
  }
};