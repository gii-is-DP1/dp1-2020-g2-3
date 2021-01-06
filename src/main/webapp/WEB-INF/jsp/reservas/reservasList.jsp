
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="Reserva">
    <h2>Reservas</h2>

    <table id="reservasTable" class="table table-striped">
        <thead>
        <tr>
         	<th style="width: 100px;">Cliente</th>
          	<th style="width: 100px;">Ruta</th>
            <th style="width: 200px;">Fecha Salida</th>
            <th style="width: 200px;">Fecha Llegada</th>
            <th style="width: 100px;">Hora Salida</th>
            <th style="width: 100px;">Hora Llegada</th>
            <th style="width: 100px;">Hora Espera</th>
            <th style="width: 150px;">Plazas Ocupadas</th>
            <th style="width: 200px;">Descripción Equipaje</th>
            <th style="width: 200px;">Estado Reserva</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${reserva}" var="reserva">
            <tr>
            	<td>
                    <c:out value="${reserva.cliente.id}"/>
                </td>
                <td>
                    <c:out value="${reserva.ruta.id}"/>
                </td>
                <td>
                    <c:out value="${reserva.fechaSalida}"/>
                </td>
                <td>
                    <c:out value="${reserva.fechaLlegada}"/>
                </td>
                 <td>
                    <c:out value="${reserva.horaSalida}"/>
                </td>
                 <td>
                    <c:out value="${reserva.horaLlegada}"/>
                </td>
                 <td>
                    <c:out value="${reserva.horasEspera}"/>
                </td>
                  <td>
                    <c:out value="${reserva.plazas_Ocupadas}"/>
                </td>
                  <td>
                    <c:out value="${reserva.descripcionEquipaje}"/>
                </td>
                  <td>
                    <c:out value="${reserva.estadoReserva}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    
    <p>
    	<a href="/reservas/new" class="btn  btn-success"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span>Añadir Reserva</a>
    </p>
</petclinic:layout>
