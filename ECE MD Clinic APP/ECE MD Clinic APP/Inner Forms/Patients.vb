Public Class Patients

    Private Sub Patients_Load(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles MyBase.Load

    End Sub

    Private Sub Button1_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Button1.Click
        New_Patient.ShowDialog()
    End Sub

    Private Sub Button2_Click(ByVal sender As System.Object, ByVal e As System.EventArgs)
        Dim p_history As New View_patient
        p_history.Show()
    End Sub

    Private Sub txt_search_Enter(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_search.Enter
        With txt_search
            .Clear()
            .ForeColor = Color.Black
        End With
    End Sub

    Private Sub txt_search_Leave(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_search.Leave
        If txt_search.Text = "" Then
            With txt_search
                .Text = "Search Patient here"
                .ForeColor = Color.Gray
            End With
        End If
    End Sub
End Class