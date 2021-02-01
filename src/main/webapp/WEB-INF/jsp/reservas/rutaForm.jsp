<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


 <input type="hidden" name="numCiudadesIntermedias" value="${numCiudadesIntermedias}"/>
      
       
        <label for="ruta.origenCliente">Ciudad Origen:</label>
		<select required="true" name="ruta.origenCliente" id="ruta.origenCliente">
				 <c:forEach items="${paradas}" var="parada">
				 <c:choose>
                    <c:when test="${parada == reserva.ruta.origenCliente}">
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
		<label for="ruta.trayectos[${i}].origen">Parada ${i}:</label>
		<select required="true" name="ruta.trayectos[${i}].origen" id="ruta.trayectos[${i}].origen">
				 <c:forEach items="${paradas}" var="parada">
				 <c:choose>
                    <c:when test="${parada == reserva.ruta.trayectos[i].origen}">
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
        <label for="ruta.destinoCliente">Ciudad Destino:</label>
		<select required="true" name="ruta.destinoCliente" id="ruta.destinoCliente">
		 	<c:forEach items="${paradas}" var="parada">
		 		<c:choose>
                    <c:when test="${parada == reserva.ruta.destinoCliente}">
                        <option value="${parada}" selected > ${parada}</option>
                    </c:when>
                    <c:otherwise>
                        <option value="${parada}"> ${parada}</option>
                    </c:otherwise>
               	 </c:choose>
    		 </c:forEach>
		</select>