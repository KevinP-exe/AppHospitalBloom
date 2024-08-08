create table paciente (
UUID_Paciente varchar2(50) primary key not null,
Nombres_Paciente varchar2(150),
Apellidos_Paciente varchar2(150),
Edad_Paciente int,
Fecha_Nacimiento varchar2(200),
Enfermedad varchar2(150),
Habitacion int,
Cama int
);

create table medicamento (
UUID_Medicamento varchar2(50) primary key not null,
Nombre_Medicamento varchar2(150)
);

create table recetado (
UUID_Receta varchar2(50) primary key not null,
UUID_Paciente varchar2(150),
UUID_Medicamento varchar2(150),
Hora_Aplicacion varchar2(200)
);

ALTER TABLE recetado
ADD CONSTRAINT fk_paciente
FOREIGN KEY (UUID_Paciente) REFERENCES paciente(UUID_Paciente);

ALTER TABLE recetado
ADD CONSTRAINT fk_medicamento
FOREIGN KEY (UUID_Medicamento) REFERENCES medicamento(UUID_Medicamento);