<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="reservas">
    <form:form modelAttribute="reserva" class="form-horizontal" id="add-reserva-form" action="/reservas/redirigir">
        
         <input type="hidden" name="numCiudadesIntermedias" value="${numCiudadesIntermedias}"/>
        
        <label for="origenCliente">Ciudad Origen:</label>
		<select name="origenCliente" id="origenCliente">
				 <c:forEach items="${paradas}" var="parada">
				 <c:choose>
                    <c:when test="${parada == ruta.origenCliente}">
                        <option value="${parada}" selected > ${parada}</option>
                    </c:when>
                    <c:otherwise>
                        <option value="${parada}"> ${parada}</option>
                    </c:otherwise>
               	 </c:choose>
    	        </c:forEach>
		</select>
		<br> <br>

 <c:choose>
	<c:when test = "${numCiudadesIntermedias>0}">
		<c:forEach var="i" begin="0" end="${finBucle}" step="1" varStatus ="status">
		<label for="trayectos[${i}].origen">Parada ${i}:</label>
		<select name="trayectos[${i}].origen" id="trayectos[${i}].origen">
				 <c:forEach items="${paradas}" var="parada">
				 <c:choose>
                    <c:when test="${parada == ruta.trayectos[i].origen}">
                        <option value="${parada}" selected > ${parada}</option>
                    </c:when>
                    <c:otherwise>
                        <option value="${parada}"> ${parada}</option>
                    
                    </c:otherwise>
               	 </c:choose>
    	        </c:forEach>
		</select>
		<br> 
		</c:forEach>	
	</c:when>
</c:choose>
    	    <br> 
		<button class="btn btn-default" type="submit" name="action" value="addParada">Añadir parada intermedia +</button> 		 
		 <br><br>
        <label for="destinoCliente">Ciudad Destino:</label>
		<select name="destinoCliente" id="destinoCliente">
		 	<c:forEach items="${paradas}" var="parada">
		 		<c:choose>
                    <c:when test="${parada == ruta.destinoCliente}">
                        <option value="${parada}" selected > ${parada}</option>
                    </c:when>
                    <c:otherwise>
                        <option value="${parada}"> ${parada}</option>
                    </c:otherwise>
               	 </c:choose>
    		 </c:forEach>
		</select>
		<br><br>
		
		<!--  Poner de tipo DATE, tener en cuenta que html te da el formato dd/MM/yyyy -->
		
           <label for="fechaSalida">Fecha de salida</label>
           <input type="date" name="fechaSalida" id="fechaSalida" placeholder="yyyy/MM/dd"/>
              <label for="horaSalida">Hora de salida</label>
           <input type="time" name="horaSalida" id="horaSalida"/>
         <br><br>
            <petclinic:inputField label="Plazas ocupadas" name="plazas_Ocupadas"/>
            <petclinic:inputField label="Descripción del equipaje" name="descripcionEquipaje"/>
			
     		
    	    <!-- Vamos a tener 2 botones submit  en el mismo formulario  -->
    	       
		<button class="btn btn-default" type="submit" name="action" value="continuar">Calcular Precio</button> 		 
    </form:form>    
    
</petclinic:layout>