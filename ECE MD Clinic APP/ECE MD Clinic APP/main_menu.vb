Public Class main_menu

    Private Sub main_menu_Load(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles MyBase.Load
        'connect()
    End Sub

    Private Sub TabControl1_SelectedIndexChanged(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles TabControl1.SelectedIndexChanged
        Select Case TabControl1.SelectedIndex
            Case 0
                
            Case 1
                Patients.MdiParent = Me
                Patients.Parent = Me.TabPage2
                Patients.Show()
            Case 2
                Consultation_Schedule.MdiParent = Me
                Consultation_Schedule.Parent = Me.TabPage3
                Consultation_Schedule.Dock = DockStyle.Fill
                Consultation_Schedule.Show()
        End Select
    End Sub

End Class