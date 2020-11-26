<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="automoviles">
    <form:form modelAttribute="automovil" class="form-horizontal" id="add-auto-form">
        
            <petclinic:inputField label="Marca" name="marca"/>
            <petclinic:inputField label="Modelo" name="modelo"/>
            <petclinic:inputField label="Número de plazas" name="numPlazas"/>
            <petclinic:inputField label="Kilómetros recorridos" name="kmRecorridos"/>
          
			     
	 <!--  El trabajador correspondiente aparecerá seleccionado por defecto-->
	 
		 <label for="trabajador">Trabajador:</label>
		<select name="trabajador" id="trabajador">
				 <c:forEach items="${trabajadores}" var="trabajador">
				  <c:choose>
                    <c:when test="${trabajador.id == automovil.trabajador.id}">
                        <option value="${trabajador.id}" selected >
				 ${trabajador.nombre} <p> </p> ${trabajador.apellidos}</option>
                    </c:when>
                    <c:otherwise>
                    <option value="${trabajador.id}"  >
				 ${trabajador.nombre} <p> </p> ${trabajador.apellidos}</option>
                    </c:otherwise>
                </c:choose>
				 
				 <option value="${trabajador.id}" >
				 ${trabajador.nombre} <p> </p> ${trabajador.apellidos}</option>
				 
    	        </c:forEach>
		</select>

             <input type="hidden" name="id" id="id" value="${automovil.id}"/>
             <c:choose>
                    <c:when test="${automovil['new']}">
                        <button class="btn btn-default" type="submit">Añadir automóvil</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Actualizar automóvil</button>
                    </c:otherwise>
                </c:choose>
    </form:form>
</petclinic:layout>