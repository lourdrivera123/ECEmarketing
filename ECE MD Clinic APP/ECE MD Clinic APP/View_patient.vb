Public Class View_patient
    Private selected_index As Integer = 0
    Private Sub Patient_History_Load(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles MyBase.Load
        load_tab(selected_index)
    End Sub
    Private Sub load_tab(ByRef selected_tab As Integer)
        If selected_tab = 0 Then
            If selected_index = 0 Then
                tab_gen_info.BackColor = Color.Aquamarine
                tab_history.BackColor = Color.White
                tab_test_result.BackColor = Color.White
                patient_gen_info.MdiParent = Me
                patient_gen_info.Parent = Me.patient_info_container
                patient_gen_info.Show()
                patient_history.Hide()
                test_result.Hide()
            End If
        ElseIf selected_tab = 1 Then
            If selected_index = 1 Then
                tab_history.BackColor = Color.Aquamarine
                tab_gen_info.BackColor = Color.White
                tab_test_result.BackColor = Color.White
                patient_history.MdiParent = Me
                patient_history.Parent = Me.patient_info_container
                patient_history.Show()
                patient_gen_info.Hide()
            End If
        ElseIf selected_tab = 2 Then
            If selected_index = 2 Then
                tab_test_result.BackColor = Color.Aquamarine
                tab_history.BackColor = Color.White
                tab_history.BackColor = Color.White
                test_result.MdiParent = Me
                test_result.Parent = Me.patient_info_container
                test_result.Show()
                patient_gen_info.Hide()
                patient_history.Hide()
            End If
        End If
    End Sub
    Private Sub gen_info_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles tab_gen_info.Click
        selected_index = 0
        load_tab(selected_index)
    End Sub

    Private Sub tab_history_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles tab_history.Click
        selected_index = 1
        load_tab(selected_index)
    End Sub

    Private Sub tab_test_result_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles tab_test_result.Click
        selected_index = 2
        load_tab(selected_index)
    End Sub
End Class