import { DynamoDBClient } from "@aws-sdk/client-dynamodb";
import { DynamoDBDocumentClient, PutCommand, ScanCommand } from "@aws-sdk/lib-dynamodb";

const client = new DynamoDBClient({});
const docClient = DynamoDBDocumentClient.from(client);
const TABLE_NAME = "ventas_spa";

export const insertarVenta = async (venta) => {
  const command = new PutCommand({ TableName: TABLE_NAME, Item: venta });
  return await docClient.send(command);
};

export const getVentas = async () => {
  const command = new ScanCommand({ TableName: TABLE_NAME });
  return await docClient.send(command);
};
