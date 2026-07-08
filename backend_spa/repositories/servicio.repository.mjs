import {
  PutCommand,
  GetCommand,
  UpdateCommand,
  DeleteCommand,
  QueryCommand,
  ScanCommand
} from "@aws-sdk/lib-dynamodb";
import { ddb } from "../config/dynamo.mjs";

const TABLE = "servicios_spa";

export const insertarServicio = (item) =>
  ddb.send(new PutCommand({
    TableName: TABLE,
    Item: item,
  }));

// CORREGIDO: Cambiado de 'id' a 'nombre' para buscar
export const getServicioByNombre = (nombre) =>
  ddb.send(new GetCommand({
    TableName: TABLE,
    Key: { nombre }, 
  }));

export const eliminarServicio = (nombre) =>
  ddb.send(new DeleteCommand({
    TableName: TABLE,
    Key: { nombre },
    ConditionExpression: "attribute_exists(nombre)" 
  }));

export const actualizarServicio = (nombre, updateExpression, names, values) =>
ddb.send(new UpdateCommand({
  TableName: TABLE,
  Key: { nombre },
  UpdateExpression: updateExpression,
  ExpressionAttributeNames: names,
  ExpressionAttributeValues: values,

  ReturnValues: "ALL_NEW",
}));

export const getServicios = (limit, lastKey) =>
  ddb.send(new ScanCommand({
    TableName: TABLE,
    Limit: limit,
    ExclusiveStartKey: lastKey ? JSON.parse(Buffer.from(lastKey, "base64").toString()) : undefined,
  }));