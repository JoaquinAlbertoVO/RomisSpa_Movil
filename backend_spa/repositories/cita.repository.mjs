import { DynamoDBClient } from "@aws-sdk/client-dynamodb";
import { DynamoDBDocumentClient, PutCommand, ScanCommand, DeleteCommand, UpdateCommand } from "@aws-sdk/lib-dynamodb";

const client = new DynamoDBClient({});
const docClient = DynamoDBDocumentClient.from(client);
const TABLE_NAME = "citas_spa";

export const insertarCita = async (cita) => {
  const command = new PutCommand({ TableName: TABLE_NAME, Item: cita });
  return await docClient.send(command);
};

export const getCitas = async () => {
  const command = new ScanCommand({ TableName: TABLE_NAME });
  return await docClient.send(command);
};

export const eliminarCita = async (id) => {
  const command = new DeleteCommand({ TableName: TABLE_NAME, Key: { id } });
  return await docClient.send(command);
};

export const actualizarEstadoCita = async (id, estado) => {
  const command = new UpdateCommand({
    TableName: TABLE_NAME,
    Key: { id },
    UpdateExpression: "set estado = :e",
    ExpressionAttributeValues: {
      ":e": estado,
    },
    ReturnValues: "ALL_NEW",
  });
  return await docClient.send(command);
};
