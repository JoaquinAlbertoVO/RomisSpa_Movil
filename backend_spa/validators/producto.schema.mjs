import { z } from "zod";

export const productoSchema = z.object({
  id: z.string().optional().nullable(),
  nombre: z.string().min(1, "El nombre es obligatorio"),
  descripcion: z.string().optional(),
  precio: z.number().min(0),
  stock: z.number().int().min(0),
  categoria: z.string().min(1, "La categoría es obligatoria")
});
