<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="servicios">
    <form:form modelAttribute="servicio" class="form-horizontal" id="add-service-form">
        
           
        	<label for="fecha">Fecha</label>
            <input type="date" name="fecha"  id="fecha" value="<fmt:formatDate pattern = "yyyy-MM-dd" value = "${servicio.fecha}" />"/>
            
            <petclinic:inputField label="Precio" name="precio"/>
            <petclinic:inputField label="Descripción" name="descripcion"/>
            
            <label for="fechaCompletado">Fecha Completado</label>
            <input type="date" name="fechaCompletado"  id="fechaCompletado" value="<fmt:formatDate pattern = "yyyy-MM-dd" value = "${servicio.fechaCompletado}" />"/>
		
			 <label for="trabajador">Trabajador:</label>
		<select name="trabajador" id="trabajador">
				 <c:forEach items="${trabajadores}" var="trabajador">
				  <c:choose>
                    <c:when test="${trabajador.id == servicio.trabajador.id}">
                        <option value="${trabajador.id}" selected >
				 ${trabajador.nombre} <p> </p> ${trabajador.apellidos}</option>
                    </c:when>
                    <c:otherwise>
                    <option value="${trabajador.id}"  >
				 ${trabajador.nombre} <p> </p> ${trabajador.apellidos}</option>
                    </c:otherwise>
                </c:choose>
				 
    	        </c:forEach>
		</select>
		
		 <label for="automovil">Automovil:</label>
		<select name="automovil" id="automovil">
				 <c:forEach items="${automoviles}" var="automovil">
				  <c:choose>
                    <c:when test="${automovil.id == servicio.automovil.id}">
                        <option value="${automovil.id}" selected >
				 ${automovil.marca} <p> </p> ${automovil.modelo}</option>
                    </c:when>
                    <c:otherwise>
                    <option value="${automovil.id}"  >
				 ${automovil.marca} <p> </p> ${automovil.modelo}</option>
                    </c:otherwise>
                </c:choose>
				 
    	        </c:forEach>
		</select>
		
		 <label for="taller">Taller:</label>
		<select name="taller" id="taller">
				 <c:forEach items="${talleres}" var="taller">
				  <c:choose>
                    <c:when test="${taller.id == servicio.taller.id}">
                        <option value="${taller.id}" selected >
				 ${taller.name} </option>
                    </c:when>
                    <c:otherwise>
                    <option value="${taller.id}"  >
				 ${taller.name}</option>
                    </c:otherwise>
                </c:choose>
				 
    	        </c:forEach>
		</select>
		
		
		 <label for="completado">Completado:</label>
		<select name="completado" id="completado">
			
            <option value="true"  >Completado</option>
				
                
            <option value="false" selected >No Completado</option>
		                      
		</select>
		


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