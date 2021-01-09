<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="trabajadores">
    <h2>Trabajadores</h2>

    <table id="trabajadoresTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">DNI</th>
            <th style="width: 200px;">Nombre</th>
            <th style="width: 200px;">Apellidos</th>
            <th style="width: 200px;">Correo Electronico</th>
            <th style="width: 200px;">Telefono</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${trabajadores}" var="trabajador">
            <tr>
                <td>
                    <c:out value="${trabajador.dni}"/>
                </td>
                <td>
                    <c:out value="${trabajador.nombre}"/>
                </td>
                 <td>
                    <c:out value="${trabajador.apellidos}"/>
                </td>
                 <td>
                    <c:out value="${trabajador.email}"/>
                </td>
                 <td>
                    <c:out value="${trabajador.telefono}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    
    
<p>
    	<a href="/trabajadores/new" class="btn  btn-success"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span>Añadir Trabajador</a>
    </p>
</petclinic:layout>
