<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="reservas">
    <form:form modelAttribute="reserva" class="form-horizontal" id="add-reserva-form" action="/reservas/redirigirEditReservaForm">
        
       
        
       <h2>Ruta establecida</h2>
               <input type="hidden" name="id" id="id" value="${reserva.id}"/>
       
        <input type="hidden" name="numCiudadesIntermedias" id="numCiudadesIntermedias" value="${numCiudadesIntermedias}"/>
      <jsp:include page="/WEB-INF/jsp/reservas/mostrarRuta.jsp"/>

      <br>
      
     <petclinic:inputField label="Kil�metros totales" name="numKmTotales"/>
             <p>  Duraci�n del viaje:  ${horasRutaCliente} horas y ${minutosRutaCliente} minutos </p> <br>
             
            <input type="hidden" name="horasRutaCliente" id="horasRutaCliente" value="${horasRutaCliente}"/>
              <input type="hidden" name="minutosRutaCliente" id="minutosRutaCliente" value="${minutosRutaCliente}"/>
      
<button class="btn btn-info" type="submit" name="action" value="editarRuta"><span class="glyphicon glyphicon-edit" aria-hidden="false"></span> Editar Ruta  </button> 
<br><br>
       <p style="border:3px; border-style:solid; border-color:#000000; background-color:#FFF699">
       *Para el c�lculo de kil�metros totales se tiene en cuenta el <span style="color:green">  trayecto que realiza el taxista para llegar al origen </span>,
       as� como el de <span style="color:green"> vuelta hasta su localidad (Zah�nos) </span> <br>
       **Los trayectos en verde no le corresponden al cliente <br>
      ***La duraci�n del viaje, as� como la fecha estimada de llegada, corresponde a la ruta realizada por el cliente (Trayectos en negro)</p>
       		
      			
      <br>
       <h2>Fecha de Salida</h2>
       
        <label for="fechaSalida">Fecha de salida</label>
         <input type="date" name="fechaSalida"  id="fechaSalida" value="<fmt:formatDate pattern = "yyyy-MM-dd" value = "${reserva.fechaSalida}" />"/>     
      
       
		<label for="horaSalida">Hora de salida</label> 
           <input type="time"  name="horaSalida" id="horaSalida" value="<fmt:formatDate type = "time" pattern="HH:mm" value = "${reserva.horaSalida}" />"/>
           
		<h2>Fecha Estimada de Llegada</h2>

		
		 <label for="fechaLlegada">Fecha de llegada</label>
            <input type="date" name="fechaLlegada"  id="fechaLlegada" value="<fmt:formatDate pattern = "yyyy-MM-dd" value = "${reserva.fechaLlegada}" />"/>  
		
		<label for="horaLlegada">Hora de llegada</label> 
           <input type="time"  name="horaLlegada" id="horaLlegada" value="<fmt:formatDate type = "time" pattern="HH:mm" value = "${reserva.horaLlegada}" />"/>

        <h2> Detalles de la reserva </h2>
        
      
        
        
         	<petclinic:inputField label="Horas de espera" name="horasEspera"/>
            <petclinic:inputField label="Plazas ocupadas" name="plazas_Ocupadas"/>
            <petclinic:inputField label="Descripci�n del equipaje" name="descripcionEquipaje"/>
			
			
 		<petclinic:inputField label="Precio Total" name="precioTotal"/>
 		  <label for="estadoReserva">Estado de la reserva:</label>
		<select required="true" name="estadoReserva" id="estadoReserva">
				 <c:forEach items="${estadosReserva}" var="estado">
				 <c:choose>
                    <c:when test="${estado.id == reserva.estadoReserva.id}">
                        <option value="${estado.id}" selected > ${estado.name}</option>
                    </c:when>
                    <c:otherwise>
                        <option value="${estado.id}"> ${estado.name}</option>
                    </c:otherwise>
               	 </c:choose>
    	        </c:forEach>
		</select>
		
		<label for="cliente"> Cliente:</label>
		<select required="true" name="cliente" id="cliente">
				 <c:forEach items="${clientes}" var="cliente">
				 <c:choose>
                    <c:when test="${cliente.id == reserva.cliente.id}">
                        <option value="${cliente.id}" selected > ${cliente.user.username}</option>
                    </c:when>
                    <c:otherwise>
                        <option value="${cliente.id}"> ${cliente.user.username}</option>
                    </c:otherwise>
               	 </c:choose>
    	        </c:forEach>
		</select>
		<br><br>
	<span> &nbsp;&nbsp;<button class="btn btn-default" type="submit" name="action" value="guardarReserva">Guardar reserva</button> </span> 
     	 
     	 
    	
    </form:form>    
    
</petclinic:layout>