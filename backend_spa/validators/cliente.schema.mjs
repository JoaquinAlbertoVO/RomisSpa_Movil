import { z } from "zod";

export const clienteSchema = z.object({
  telefono: z.string().min(6, "El teléfono debe tener al menos 6 dígitos"),
  nombre: z.string().min(2, "El nombre es obligatorio"),
  ultimaVisita: z.string().optional().default("Sin visitas"),
  totalVisitas: z.number().int().optional().default(0)
});