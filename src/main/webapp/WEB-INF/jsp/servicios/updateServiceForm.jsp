<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="servicios">
    <form:form modelAttribute="servicio" class="form-horizontal" id="add-service-form">
        
            <petclinic:inputField label="Nombre" name="nombre"/>
            <petclinic:inputField label="Fecha" name="fecha"/>
            <petclinic:inputField label="Precio" name="precio"/>
            <petclinic:inputField label="Solictante" name="solictante"/>
          	<petclinic:inputField label="Automovil" name="automovil"/>
            <petclinic:inputField label="Taller" name="taller"/>
            <petclinic:inputField label="Descripción" name="descripcion"/>
			     <!-- Lo pongo de forma manual porque el form:select de Spring da error -->
		<!--  	     
		 <label for="trabajador">Trabajador:</label>
		<select name="trabajador" id="trabajador">
				 <c:forEach items="${trabajadores}" var="trabajador">
				 <option value="${trabajador.id}">${trabajador.nombre} <p> </p> ${trabajador.apellidos}</option>
    	        </c:forEach>
		</select>
			-->
             <input type="hidden" name="id" id="id" value="${servicio.id}"/>
             <c:choose>
                    <c:when test="${servicio['new']}">
                        <button class="btn btn-default" type="submit">Añadir servicio</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Actualizar servicio</button>
                    </c:otherwise>
                </c:choose>
    </form:form>
</petclinic:layout>