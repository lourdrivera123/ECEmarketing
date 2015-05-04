Imports MySql.Data.MySqlClient
Module db_connection
    Public conn As New MySqlConnection
    Public Sub connect()
        Dim dbname As String = "ece_pharmacy_tree"
        Dim dbhost As String = "127.0.0.1"
        Dim user As String = "root"
        Dim pass As String = "12345"
        Try
            conn.ConnectionString = String.Format("Server={0};Database={3};Uid={1}; Pwd={2};", dbhost, user, pass, dbname)
            conn.Open()
            conn.Close()
            MsgBox("Connected")
        Catch ex As Exception
            MsgBox(ex.ToString)
        End Try
    End Sub
End Module
