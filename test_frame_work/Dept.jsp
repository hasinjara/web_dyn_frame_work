<%@page import="table.*" %>


<p>Dept dept</p>
<% 
// out.println(request.getAttribute("list"));
Dept[] lists = (Dept[])request.getAttribute("list");
for(int i = 0; i<lists.length; i++) {
    out.println( ((Dept)lists[i]).getName());
}
%>