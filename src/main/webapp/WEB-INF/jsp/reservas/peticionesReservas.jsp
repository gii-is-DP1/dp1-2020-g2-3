<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="reservas">
    
     
    <br> 
	<h2>Reservas solicitadas</h2>
	
    <table id="reservasTable" class="table table-striped">
        <thead>
        <tr>
         	<th style="width: 100px;">Cliente</th>
          	<th style="width: 100px;">Origen</th>
            <th style="width: 200px;">Destino</th>
            <th style="width: 200px;">Fecha Salida</th>
            <th style="width: 100px;">Hora Salida</th>
            <th style="width: 100px;">Fecha llegada</th>
            <th style="width: 100px;">Hora llegada</th>
            <th style="width: 150px;">Plazas Ocupadas</th>
            <th style="width: 200px;">Precio total</th>
            <th style="width: 200px;">Estado Reserva</th>
             <th style="width: 200px;">Detalles/Editar</th>
              <th style="width: 200px;">Aceptar/Rechazar</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${reservas}" var="reserva">
            <tr>
            	<td>
                    <c:out value="${reserva.cliente.user.username}"/>
                </td>
                <td>
                    <c:out value="${reserva.ruta.origenCliente}"/>
                </td>
                <td>
                    <c:out value="${reserva.ruta.destinoCliente}"/>
                </td>
                <td>
                    <c:out value="${reserva.fechaSalida}"/>
                </td>
                 <td>
                    <c:out value="${reserva.horaSalida}"/>
                </td>
                 <td>
                    <c:out value="${reserva.fechaLlegada}"/>
                </td>
                 <td>
                    <c:out value="${reserva.horaLlegada}"/>
                </td>
                  <td>
                    <c:out value="${reserva.plazas_Ocupadas}"/>
                </td>
                  <td>
                    <c:out value="${reserva.precioTotal}"/>
                </td>
                  <td>
                    <c:out value="${reserva.estadoReserva}"/>
                </td>
                 <td>
                 
                  <spring:url value="/reservas/edit/{reservaId}" var="reservaEditUrl">
                   <spring:param name="reservaId" value="${reserva.id}"/>                  
                </spring:url>
                  <a  class="btn btn-info" href="${fn:escapeXml(reservaEditUrl)}"> <span class="glyphicon glyphicon-eye-open" aria-hidden="false"> <span class="glyphicon glyphicon-edit" aria-hidden="false"></a>
                </td>
                <td>
                
                <spring:url value="/reservas/aceptar/{reservaId}" var="aceptarReservaUrl">
                   <spring:param name="reservaId" value="${reserva.id}"/>                  
                </spring:url>
                  <a class="btn btn-success" href="${fn:escapeXml(aceptarReservaUrl)}"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span></a>
                 <spring:url value="/reservas/rechazar/{reservaId}" var="rechazarReservaUrl">
                <spring:param name="reservaId" value="${reserva.id}"/>
                </spring:url>
                <a class="btn btn-danger" href="${fn:escapeXml(rechazarReservaUrl)}"><span class="glyphicon glyphicon-remove" aria-hidden="false"></span></a>
                
                </td>
            </tr>
            <td>
            
        </c:forEach>
        </tbody>
    </table>
    
   
</petclinic:layout>