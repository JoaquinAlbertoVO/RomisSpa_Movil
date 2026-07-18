import { z } from "zod";

export const empleadoSchema = z.object({
  id: z.string().optional().nullable(),
  nombre: z.string().min(1, "El nombre es obligatorio"),
  especialidad: z.string().min(1, "La especialidad es obligatoria"),
  telefono: z.string().min(1, "El teléfono es obligatorio"),
  comision: z.number().min(0).max(100)
});
