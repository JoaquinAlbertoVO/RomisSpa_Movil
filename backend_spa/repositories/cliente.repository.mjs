import { PutCommand, ScanCommand } from "@aws-sdk/lib-dynamodb";
import { ddb } from "../config/dynamo.mjs";

const TABLE = "clientes_spa";

// 1. Inserta o actualiza el cliente usando sus campos reales
export const insertarCliente = (item) =>
  ddb.send(new PutCommand({
    TableName: TABLE,
    Item: {
      telefono: String(item.telefono),
      nombre: item.nombre,
      ultimaVisita: item.ultimaVisita || "Sin visitas",
      totalVisitas: Number(item.totalVisitas) || 0
    },
  }));

// 2. Trae todos los clientes de la tabla para que tu servicio extraiga el `.Items`
export const getClientes = (limit, lastKey) =>
  ddb.send(new ScanCommand({
    TableName: TABLE,
    Limit: limit ? Number(limit) : undefined,
    ExclusiveStartKey: lastKey ? JSON.parse(Buffer.from(lastKey, "base64").toString()) : undefined,
  }));