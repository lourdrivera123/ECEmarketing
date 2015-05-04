<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()> _
Partial Class View_patient
    Inherits System.Windows.Forms.Form

    'Form overrides dispose to clean up the component list.
    <System.Diagnostics.DebuggerNonUserCode()> _
    Protected Overrides Sub Dispose(ByVal disposing As Boolean)
        Try
            If disposing AndAlso components IsNot Nothing Then
                components.Dispose()
            End If
        Finally
            MyBase.Dispose(disposing)
        End Try
    End Sub

    'Required by the Windows Form Designer
    Private components As System.ComponentModel.IContainer

    'NOTE: The following procedure is required by the Windows Form Designer
    'It can be modified using the Windows Form Designer.  
    'Do not modify it using the code editor.
    <System.Diagnostics.DebuggerStepThrough()> _
    Private Sub InitializeComponent()
        Dim resources As System.ComponentModel.ComponentResourceManager = New System.ComponentModel.ComponentResourceManager(GetType(View_patient))
        Me.PictureBox1 = New System.Windows.Forms.PictureBox()
        Me.patient_info_container = New System.Windows.Forms.Panel()
        Me.tab_gen_info = New System.Windows.Forms.Button()
        Me.tab_history = New System.Windows.Forms.Button()
        Me.tab_test_result = New System.Windows.Forms.Button()
        CType(Me.PictureBox1, System.ComponentModel.ISupportInitialize).BeginInit()
        Me.SuspendLayout()
        '
        'PictureBox1
        '
        Me.PictureBox1.BackgroundImage = CType(resources.GetObject("PictureBox1.BackgroundImage"), System.Drawing.Image)
        Me.PictureBox1.Location = New System.Drawing.Point(23, 12)
        Me.PictureBox1.Name = "PictureBox1"
        Me.PictureBox1.Size = New System.Drawing.Size(263, 248)
        Me.PictureBox1.SizeMode = System.Windows.Forms.PictureBoxSizeMode.StretchImage
        Me.PictureBox1.TabIndex = 1
        Me.PictureBox1.TabStop = False
        '
        'patient_info_container
        '
        Me.patient_info_container.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D
        Me.patient_info_container.Location = New System.Drawing.Point(292, 12)
        Me.patient_info_container.Name = "patient_info_container"
        Me.patient_info_container.Size = New System.Drawing.Size(802, 539)
        Me.patient_info_container.TabIndex = 2
        '
        'tab_gen_info
        '
        Me.tab_gen_info.BackColor = System.Drawing.Color.Aquamarine
        Me.tab_gen_info.Location = New System.Drawing.Point(107, 285)
        Me.tab_gen_info.Name = "tab_gen_info"
        Me.tab_gen_info.Size = New System.Drawing.Size(179, 42)
        Me.tab_gen_info.TabIndex = 3
        Me.tab_gen_info.Text = "General Patient Info"
        Me.tab_gen_info.UseVisualStyleBackColor = False
        '
        'tab_history
        '
        Me.tab_history.Location = New System.Drawing.Point(107, 339)
        Me.tab_history.Name = "tab_history"
        Me.tab_history.Size = New System.Drawing.Size(179, 42)
        Me.tab_history.TabIndex = 4
        Me.tab_history.Text = "History"
        Me.tab_history.UseVisualStyleBackColor = True
        '
        'tab_test_result
        '
        Me.tab_test_result.Location = New System.Drawing.Point(107, 397)
        Me.tab_test_result.Name = "tab_test_result"
        Me.tab_test_result.Size = New System.Drawing.Size(179, 42)
        Me.tab_test_result.TabIndex = 5
        Me.tab_test_result.Text = "Test Result"
        Me.tab_test_result.UseVisualStyleBackColor = True
        '
        'View_patient
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.ClientSize = New System.Drawing.Size(1122, 628)
        Me.Controls.Add(Me.tab_test_result)
        Me.Controls.Add(Me.tab_history)
        Me.Controls.Add(Me.tab_gen_info)
        Me.Controls.Add(Me.patient_info_container)
        Me.Controls.Add(Me.PictureBox1)
        Me.IsMdiContainer = True
        Me.Name = "View_patient"
        Me.ShowIcon = False
        Me.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen
        Me.Text = "Patient Record"
        CType(Me.PictureBox1, System.ComponentModel.ISupportInitialize).EndInit()
        Me.ResumeLayout(False)

    End Sub
    Friend WithEvents PictureBox1 As System.Windows.Forms.PictureBox
    Friend WithEvents patient_info_container As System.Windows.Forms.Panel
    Friend WithEvents tab_gen_info As System.Windows.Forms.Button
    Friend WithEvents tab_history As System.Windows.Forms.Button
    Friend WithEvents tab_test_result As System.Windows.Forms.Button
End Class
