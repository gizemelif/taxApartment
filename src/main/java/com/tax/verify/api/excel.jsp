<c:url value="/uploadExcelFile" var="uploadFileUrl" />
<form method="post" enctype="multipart/form-data"
  action="${uploadFileUrl}">
    <input type="file" name="file" accept=".xls,.xlsx" /> <input
      type="submit" value="Upload file" />
</form>