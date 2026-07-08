import { z } from "zod";

export const citaSchema = z.object({
  cliente: z.string().min(1, "El nombre del cliente es obligatorio"),
  servicio: z.string().min(1, "El servicio es obligatorio"),
  fecha: z.string().min(1, "La fecha es obligatoria"),
  hora: z.string().min(1, "La hora es obligatoria")
});