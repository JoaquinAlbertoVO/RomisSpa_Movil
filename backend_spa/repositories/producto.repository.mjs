import {
  PutCommand,
  GetCommand,
  UpdateCommand,
  DeleteCommand,
  ScanCommand
} from "@aws-sdk/lib-dynamodb";
import { ddb } from "../config/dynamo.mjs";

const TABLE = "productos_spa";

export const insertarProducto = (item) =>
  ddb.send(new PutCommand({
    TableName: TABLE,
    Item: item,
  }));

export const getProductoById = (id) =>
  ddb.send(new GetCommand({
    TableName: TABLE,
    Key: { id },
  }));

export const eliminarProducto = (id) =>
  ddb.send(new DeleteCommand({
    TableName: TABLE,
    Key: { id },
    ConditionExpression: "attribute_exists(id)"
  }));

export const actualizarProducto = (id, updateExpression, names, values) =>
  ddb.send(new UpdateCommand({
    TableName: TABLE,
    Key: { id },
    UpdateExpression: updateExpression,
    ExpressionAttributeNames: names,
    ExpressionAttributeValues: values,
    ReturnValues: "ALL_NEW",
  }));

export const getProductos = (limit, lastKey) =>
  ddb.send(new ScanCommand({
    TableName: TABLE,
    Limit: limit,
    ExclusiveStartKey: lastKey ? JSON.parse(Buffer.from(lastKey, "base64").toString()) : undefined,
  }));
