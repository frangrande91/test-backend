# test-backend

MobyDigital
Test Backend Developer

Dado el siguiente enunciado...
“Un Candidato tiene conocimiento de ciertas Tecnologías. Los datos de interés y obligatorios de un Candidato son: nombre, apellido, tipo (DNI, LE, LC) y número de documento, 
fecha de nacimiento y las tecnologías que maneja. Para el caso de las Tecnologías, interesa persistir el nombre (java, python, maven, hibernate, spring) y versión. 
De la relación entre un Candidato y una Tecnología surge un atributo, el de años de experiencia, el cual indica cuántos años de experiencia posee el Candidato en dicha Tecnología. 
La relación es claramente de N a M ya que un candidato puede tener experiencia en múltiples tecnologías y una tecnología puede estar asociada a múltiples candidatos”


Para resolver lo enunciado anteriormente deberás implementar…
- API Rest (CRUD) para Candidato y Tecnología. Esto implica: Crear controladores, servicios, repositorios y entidades.
- BD en memoria (por ejemplo, H2).
- Validaciones de campos obligatorios, de fecha y númericos.
- Creación, uso y manejo de al menos 2 excepciones.
- Funcionalidad listar Candidatos de X Tecnología. Recibe un texto por ejemplo “Java” y devuelve los datos del Candidato y años de experiencia en dicha Tecnología. Si existen varias versiones de la misma tecnología, se listan aquellas que coincidan con el nombre exacto.
- Crear Logs en donde consideres.
- Exponer a través de Swagger los APIs creados.
- Test unitarios y/o de integración.
- Añadir manejo de excepciones centralizado usando aspectos. Manejar las excepciones que consideres necesarias.
- Añadir algún tipo de seguridad para la API (token spring, basic auth, roles).


#TalentoMobyDigital
