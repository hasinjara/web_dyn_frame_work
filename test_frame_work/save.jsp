<%@page import="table.*" %>

<% 
// out.println(request.getAttribute("Dept"));
    Dept dept = (Dept)request.getAttribute("Dept");
    out.println(dept.getName());
%>