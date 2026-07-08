import { z } from "zod";

export const servicioSchema = z.object({
    nombre: z.string().min(1),
    descripcion: z.string().min(1),
    precio: z.number().positive(),
    imagenRes: z.number().nullable().optional()
});