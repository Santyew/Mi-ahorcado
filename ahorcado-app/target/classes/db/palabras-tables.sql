

-- Eliminar la tabla palabra si existe
DROP TABLE IF EXISTS palabra;


-- Crear la tabla palabra
CREATE TABLE palabra (
    palabraString TEXT NOT NULL,
    dificultad TEXT  NOT NULL
);

-- Insertar usuarios de ejemplo
INSERT INTO palabra (palabraString, dificultad) VALUES
     ('perro', 'facil'),
    ('mesa', 'facil'),
    ('tenedor', 'medio'),
    ('zorro', 'dificil'),
    ('asno', 'medio'),
    ('relampagos', 'dificil'),
    ('grotesco', 'medio'),
    ('quimera', 'dificil'),
    ('rex', 'dificil'),
    ('gato', 'facil');

