<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


       
<c:choose>
 <c:when test = "${reserva.ruta.origenCliente != 'Zahinos'}"> <!--  El trayecto del taxista al origen del cliente se muestra en otro color --> 
 
  <div style="float:left">
 <p style="color:green"  > Zahinos &nbsp; <span class="glyphicon glyphicon-arrow-right" aria-hidden="false"></span>   &nbsp;</p> 
 </div>
 </c:when>
</c:choose>
       
       <input type="hidden" name="ruta.origenCliente" id="ruta.origenCliente" value="${reserva.ruta.origenCliente}"/>
       <p style="float:left"> ${reserva.ruta.origenCliente}   &nbsp; <span class="glyphicon glyphicon-arrow-right" aria-hidden="false"></span>   &nbsp;</p> 
     <c:choose>
       <c:when test = "${numCiudadesIntermedias>0}"> 
       <c:forEach var="i" begin="0" end="${finBucle}" step="1" varStatus ="status">
       
       <p style="float:left"> 
  
      	     ${trayectosIntermedios[i].origen}   &nbsp; <span class="glyphicon glyphicon-arrow-right" aria-hidden="false"></span> &nbsp; </p>
       		<input type="hidden" name="ruta.trayectos[${i}].origen" id="ruta.trayectos[${i}].origen" value="${trayectosIntermedios[i].origen}" />
           	</c:forEach>
       </c:when>
     </c:choose>
    
      <input type="hidden" name="ruta.destinoCliente" id="ruta.destinoCliente" value="${reserva.ruta.destinoCliente}"/>
       
       <p style="float:left"> ${reserva.ruta.destinoCliente} </p>
       
       <c:choose>
       
 <c:when test = "${reserva.ruta.destinoCliente != 'Zahinos'}">
  <!-- Trayecto de vuelta del taxista desde el destino del cliente hasta la localidad del taxista, zahinos-->

  <p style="color:green";"float:left">&nbsp;  <span class="glyphicon glyphicon-arrow-right" aria-hidden="false"></span> &nbsp; Zahinos </p>
 </c:when>
</c:choose>