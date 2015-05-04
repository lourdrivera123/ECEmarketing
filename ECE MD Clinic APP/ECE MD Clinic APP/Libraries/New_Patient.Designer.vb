<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()> _
Partial Class New_Patient
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
        Me.btn_next_geninfo = New System.Windows.Forms.Button()
        Me.Button3 = New System.Windows.Forms.Button()
        Me.TabControl1 = New System.Windows.Forms.TabControl()
        Me.TabPage1 = New System.Windows.Forms.TabPage()
        Me.txt_weight = New System.Windows.Forms.TextBox()
        Me.txt_height = New System.Windows.Forms.TextBox()
        Me.txt_civil_status = New System.Windows.Forms.TextBox()
        Me.GroupBox1 = New System.Windows.Forms.GroupBox()
        Me.rdbtn_female = New System.Windows.Forms.RadioButton()
        Me.rbtn_male = New System.Windows.Forms.RadioButton()
        Me.birthdate_picker = New System.Windows.Forms.DateTimePicker()
        Me.txt_occupation = New System.Windows.Forms.TextBox()
        Me.txt_lname = New System.Windows.Forms.TextBox()
        Me.txt_mname = New System.Windows.Forms.TextBox()
        Me.txt_fname = New System.Windows.Forms.TextBox()
        Me.Label15 = New System.Windows.Forms.Label()
        Me.TabPage2 = New System.Windows.Forms.TabPage()
        Me.txt_email = New System.Windows.Forms.TextBox()
        Me.txt_telno = New System.Windows.Forms.TextBox()
        Me.txt_mobileno = New System.Windows.Forms.TextBox()
        Me.txt_zip = New System.Windows.Forms.TextBox()
        Me.cmb_region = New System.Windows.Forms.ComboBox()
        Me.txt_province = New System.Windows.Forms.TextBox()
        Me.txt_city_municipality = New System.Windows.Forms.TextBox()
        Me.txt_barangay = New System.Windows.Forms.TextBox()
        Me.txt_street = New System.Windows.Forms.TextBox()
        Me.txt_houseno = New System.Windows.Forms.TextBox()
        Me.Button5 = New System.Windows.Forms.Button()
        Me.Button1 = New System.Windows.Forms.Button()
        Me.btn_next_contact_info = New System.Windows.Forms.Button()
        Me.TabPage3 = New System.Windows.Forms.TabPage()
        Me.txt_confirmpword = New System.Windows.Forms.TextBox()
        Me.txt_pword = New System.Windows.Forms.TextBox()
        Me.txt_uname = New System.Windows.Forms.TextBox()
        Me.Button6 = New System.Windows.Forms.Button()
        Me.Button7 = New System.Windows.Forms.Button()
        Me.btn_finish = New System.Windows.Forms.Button()
        Me.TabControl1.SuspendLayout()
        Me.TabPage1.SuspendLayout()
        Me.GroupBox1.SuspendLayout()
        Me.TabPage2.SuspendLayout()
        Me.TabPage3.SuspendLayout()
        Me.SuspendLayout()
        '
        'btn_next_geninfo
        '
        Me.btn_next_geninfo.Enabled = False
        Me.btn_next_geninfo.Location = New System.Drawing.Point(242, 329)
        Me.btn_next_geninfo.Name = "btn_next_geninfo"
        Me.btn_next_geninfo.Size = New System.Drawing.Size(76, 26)
        Me.btn_next_geninfo.TabIndex = 1
        Me.btn_next_geninfo.Text = "Next"
        Me.btn_next_geninfo.UseVisualStyleBackColor = True
        '
        'Button3
        '
        Me.Button3.Location = New System.Drawing.Point(324, 329)
        Me.Button3.Name = "Button3"
        Me.Button3.Size = New System.Drawing.Size(76, 26)
        Me.Button3.TabIndex = 2
        Me.Button3.Text = "Cancel"
        Me.Button3.UseVisualStyleBackColor = True
        '
        'TabControl1
        '
        Me.TabControl1.Controls.Add(Me.TabPage1)
        Me.TabControl1.Controls.Add(Me.TabPage2)
        Me.TabControl1.Controls.Add(Me.TabPage3)
        Me.TabControl1.Location = New System.Drawing.Point(0, 1)
        Me.TabControl1.Name = "TabControl1"
        Me.TabControl1.SelectedIndex = 0
        Me.TabControl1.Size = New System.Drawing.Size(414, 387)
        Me.TabControl1.TabIndex = 3
        '
        'TabPage1
        '
        Me.TabPage1.Controls.Add(Me.txt_weight)
        Me.TabPage1.Controls.Add(Me.txt_height)
        Me.TabPage1.Controls.Add(Me.txt_civil_status)
        Me.TabPage1.Controls.Add(Me.GroupBox1)
        Me.TabPage1.Controls.Add(Me.birthdate_picker)
        Me.TabPage1.Controls.Add(Me.txt_occupation)
        Me.TabPage1.Controls.Add(Me.txt_lname)
        Me.TabPage1.Controls.Add(Me.txt_mname)
        Me.TabPage1.Controls.Add(Me.txt_fname)
        Me.TabPage1.Controls.Add(Me.Label15)
        Me.TabPage1.Controls.Add(Me.Button3)
        Me.TabPage1.Controls.Add(Me.btn_next_geninfo)
        Me.TabPage1.Location = New System.Drawing.Point(4, 22)
        Me.TabPage1.Name = "TabPage1"
        Me.TabPage1.Padding = New System.Windows.Forms.Padding(3)
        Me.TabPage1.Size = New System.Drawing.Size(406, 361)
        Me.TabPage1.TabIndex = 0
        Me.TabPage1.Text = "General Patient Info"
        Me.TabPage1.UseVisualStyleBackColor = True
        '
        'txt_weight
        '
        Me.txt_weight.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend
        Me.txt_weight.ForeColor = System.Drawing.SystemColors.GrayText
        Me.txt_weight.Location = New System.Drawing.Point(72, 300)
        Me.txt_weight.Name = "txt_weight"
        Me.txt_weight.RightToLeft = System.Windows.Forms.RightToLeft.No
        Me.txt_weight.Size = New System.Drawing.Size(253, 20)
        Me.txt_weight.TabIndex = 29
        Me.txt_weight.Text = "Weight"
        '
        'txt_height
        '
        Me.txt_height.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend
        Me.txt_height.ForeColor = System.Drawing.SystemColors.GrayText
        Me.txt_height.Location = New System.Drawing.Point(74, 274)
        Me.txt_height.Name = "txt_height"
        Me.txt_height.RightToLeft = System.Windows.Forms.RightToLeft.No
        Me.txt_height.Size = New System.Drawing.Size(253, 20)
        Me.txt_height.TabIndex = 28
        Me.txt_height.Text = "Height"
        '
        'txt_civil_status
        '
        Me.txt_civil_status.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend
        Me.txt_civil_status.ForeColor = System.Drawing.SystemColors.GrayText
        Me.txt_civil_status.Location = New System.Drawing.Point(74, 248)
        Me.txt_civil_status.Name = "txt_civil_status"
        Me.txt_civil_status.RightToLeft = System.Windows.Forms.RightToLeft.No
        Me.txt_civil_status.Size = New System.Drawing.Size(253, 20)
        Me.txt_civil_status.TabIndex = 27
        Me.txt_civil_status.Text = "Civil Status"
        '
        'GroupBox1
        '
        Me.GroupBox1.Controls.Add(Me.rdbtn_female)
        Me.GroupBox1.Controls.Add(Me.rbtn_male)
        Me.GroupBox1.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.GroupBox1.Location = New System.Drawing.Point(72, 185)
        Me.GroupBox1.Name = "GroupBox1"
        Me.GroupBox1.Size = New System.Drawing.Size(255, 53)
        Me.GroupBox1.TabIndex = 26
        Me.GroupBox1.TabStop = False
        Me.GroupBox1.Text = "Sex"
        '
        'rdbtn_female
        '
        Me.rdbtn_female.AutoSize = True
        Me.rdbtn_female.Location = New System.Drawing.Point(117, 20)
        Me.rdbtn_female.Name = "rdbtn_female"
        Me.rdbtn_female.Size = New System.Drawing.Size(73, 17)
        Me.rdbtn_female.TabIndex = 1
        Me.rdbtn_female.TabStop = True
        Me.rdbtn_female.Text = "FEMALE"
        Me.rdbtn_female.UseVisualStyleBackColor = True
        '
        'rbtn_male
        '
        Me.rbtn_male.AutoSize = True
        Me.rbtn_male.Location = New System.Drawing.Point(33, 20)
        Me.rbtn_male.Name = "rbtn_male"
        Me.rbtn_male.Size = New System.Drawing.Size(58, 17)
        Me.rbtn_male.TabIndex = 0
        Me.rbtn_male.TabStop = True
        Me.rbtn_male.Text = "MALE"
        Me.rbtn_male.UseVisualStyleBackColor = True
        '
        'birthdate_picker
        '
        Me.birthdate_picker.Format = System.Windows.Forms.DateTimePickerFormat.[Short]
        Me.birthdate_picker.Location = New System.Drawing.Point(72, 151)
        Me.birthdate_picker.Name = "birthdate_picker"
        Me.birthdate_picker.Size = New System.Drawing.Size(255, 20)
        Me.birthdate_picker.TabIndex = 25
        '
        'txt_occupation
        '
        Me.txt_occupation.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend
        Me.txt_occupation.ForeColor = System.Drawing.SystemColors.GrayText
        Me.txt_occupation.Location = New System.Drawing.Point(74, 100)
        Me.txt_occupation.Name = "txt_occupation"
        Me.txt_occupation.RightToLeft = System.Windows.Forms.RightToLeft.No
        Me.txt_occupation.Size = New System.Drawing.Size(253, 20)
        Me.txt_occupation.TabIndex = 24
        Me.txt_occupation.Text = "Occupation"
        '
        'txt_lname
        '
        Me.txt_lname.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend
        Me.txt_lname.ForeColor = System.Drawing.SystemColors.GrayText
        Me.txt_lname.Location = New System.Drawing.Point(74, 74)
        Me.txt_lname.Name = "txt_lname"
        Me.txt_lname.RightToLeft = System.Windows.Forms.RightToLeft.No
        Me.txt_lname.Size = New System.Drawing.Size(253, 20)
        Me.txt_lname.TabIndex = 23
        Me.txt_lname.Text = "Last Name"
        '
        'txt_mname
        '
        Me.txt_mname.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend
        Me.txt_mname.ForeColor = System.Drawing.SystemColors.GrayText
        Me.txt_mname.Location = New System.Drawing.Point(74, 48)
        Me.txt_mname.Name = "txt_mname"
        Me.txt_mname.RightToLeft = System.Windows.Forms.RightToLeft.No
        Me.txt_mname.Size = New System.Drawing.Size(253, 20)
        Me.txt_mname.TabIndex = 22
        Me.txt_mname.Text = "Middle Name"
        '
        'txt_fname
        '
        Me.txt_fname.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend
        Me.txt_fname.ForeColor = System.Drawing.SystemColors.GrayText
        Me.txt_fname.Location = New System.Drawing.Point(74, 19)
        Me.txt_fname.Name = "txt_fname"
        Me.txt_fname.RightToLeft = System.Windows.Forms.RightToLeft.No
        Me.txt_fname.Size = New System.Drawing.Size(253, 20)
        Me.txt_fname.TabIndex = 21
        Me.txt_fname.Text = "First Name"
        '
        'Label15
        '
        Me.Label15.AutoSize = True
        Me.Label15.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label15.Location = New System.Drawing.Point(71, 135)
        Me.Label15.Name = "Label15"
        Me.Label15.Size = New System.Drawing.Size(66, 13)
        Me.Label15.TabIndex = 17
        Me.Label15.Text = "Birthdate :"
        '
        'TabPage2
        '
        Me.TabPage2.Controls.Add(Me.txt_email)
        Me.TabPage2.Controls.Add(Me.txt_telno)
        Me.TabPage2.Controls.Add(Me.txt_mobileno)
        Me.TabPage2.Controls.Add(Me.txt_zip)
        Me.TabPage2.Controls.Add(Me.cmb_region)
        Me.TabPage2.Controls.Add(Me.txt_province)
        Me.TabPage2.Controls.Add(Me.txt_city_municipality)
        Me.TabPage2.Controls.Add(Me.txt_barangay)
        Me.TabPage2.Controls.Add(Me.txt_street)
        Me.TabPage2.Controls.Add(Me.txt_houseno)
        Me.TabPage2.Controls.Add(Me.Button5)
        Me.TabPage2.Controls.Add(Me.Button1)
        Me.TabPage2.Controls.Add(Me.btn_next_contact_info)
        Me.TabPage2.Location = New System.Drawing.Point(4, 22)
        Me.TabPage2.Name = "TabPage2"
        Me.TabPage2.Padding = New System.Windows.Forms.Padding(3)
        Me.TabPage2.Size = New System.Drawing.Size(406, 361)
        Me.TabPage2.TabIndex = 1
        Me.TabPage2.Text = "Contact Info"
        Me.TabPage2.UseVisualStyleBackColor = True
        '
        'txt_email
        '
        Me.txt_email.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend
        Me.txt_email.ForeColor = System.Drawing.SystemColors.GrayText
        Me.txt_email.Location = New System.Drawing.Point(92, 269)
        Me.txt_email.Name = "txt_email"
        Me.txt_email.RightToLeft = System.Windows.Forms.RightToLeft.No
        Me.txt_email.Size = New System.Drawing.Size(238, 20)
        Me.txt_email.TabIndex = 44
        Me.txt_email.Text = "E-mail Address"
        '
        'txt_telno
        '
        Me.txt_telno.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend
        Me.txt_telno.ForeColor = System.Drawing.SystemColors.GrayText
        Me.txt_telno.Location = New System.Drawing.Point(92, 243)
        Me.txt_telno.Name = "txt_telno"
        Me.txt_telno.RightToLeft = System.Windows.Forms.RightToLeft.No
        Me.txt_telno.Size = New System.Drawing.Size(238, 20)
        Me.txt_telno.TabIndex = 43
        Me.txt_telno.Text = "Telephone No."
        '
        'txt_mobileno
        '
        Me.txt_mobileno.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend
        Me.txt_mobileno.ForeColor = System.Drawing.SystemColors.GrayText
        Me.txt_mobileno.Location = New System.Drawing.Point(92, 217)
        Me.txt_mobileno.Name = "txt_mobileno"
        Me.txt_mobileno.RightToLeft = System.Windows.Forms.RightToLeft.No
        Me.txt_mobileno.Size = New System.Drawing.Size(238, 20)
        Me.txt_mobileno.TabIndex = 42
        Me.txt_mobileno.Text = "Mobile No."
        '
        'txt_zip
        '
        Me.txt_zip.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend
        Me.txt_zip.ForeColor = System.Drawing.SystemColors.GrayText
        Me.txt_zip.Location = New System.Drawing.Point(92, 191)
        Me.txt_zip.Name = "txt_zip"
        Me.txt_zip.RightToLeft = System.Windows.Forms.RightToLeft.No
        Me.txt_zip.Size = New System.Drawing.Size(238, 20)
        Me.txt_zip.TabIndex = 41
        Me.txt_zip.Text = "Zip Code"
        '
        'cmb_region
        '
        Me.cmb_region.ForeColor = System.Drawing.Color.Gray
        Me.cmb_region.FormattingEnabled = True
        Me.cmb_region.Items.AddRange(New Object() {"Region I", "Region II", "Region III", "Region IV", "Region V", "Region VI", "Region VII", "Region VIII", "Region IX", "Region X", "Region XI", "Region XII", "Region XIII", "NCR", "CAR", "Bangsamoro"})
        Me.cmb_region.Location = New System.Drawing.Point(92, 164)
        Me.cmb_region.Name = "cmb_region"
        Me.cmb_region.Size = New System.Drawing.Size(238, 21)
        Me.cmb_region.TabIndex = 32
        Me.cmb_region.Text = "Region"
        '
        'txt_province
        '
        Me.txt_province.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend
        Me.txt_province.ForeColor = System.Drawing.SystemColors.GrayText
        Me.txt_province.Location = New System.Drawing.Point(92, 138)
        Me.txt_province.Name = "txt_province"
        Me.txt_province.RightToLeft = System.Windows.Forms.RightToLeft.No
        Me.txt_province.Size = New System.Drawing.Size(238, 20)
        Me.txt_province.TabIndex = 31
        Me.txt_province.Text = "Province"
        '
        'txt_city_municipality
        '
        Me.txt_city_municipality.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend
        Me.txt_city_municipality.ForeColor = System.Drawing.SystemColors.GrayText
        Me.txt_city_municipality.Location = New System.Drawing.Point(92, 112)
        Me.txt_city_municipality.Name = "txt_city_municipality"
        Me.txt_city_municipality.RightToLeft = System.Windows.Forms.RightToLeft.No
        Me.txt_city_municipality.Size = New System.Drawing.Size(238, 20)
        Me.txt_city_municipality.TabIndex = 30
        Me.txt_city_municipality.Text = "City/ Municipality"
        '
        'txt_barangay
        '
        Me.txt_barangay.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend
        Me.txt_barangay.ForeColor = System.Drawing.SystemColors.GrayText
        Me.txt_barangay.Location = New System.Drawing.Point(92, 86)
        Me.txt_barangay.Name = "txt_barangay"
        Me.txt_barangay.RightToLeft = System.Windows.Forms.RightToLeft.No
        Me.txt_barangay.Size = New System.Drawing.Size(238, 20)
        Me.txt_barangay.TabIndex = 29
        Me.txt_barangay.Text = "Barangay"
        '
        'txt_street
        '
        Me.txt_street.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend
        Me.txt_street.ForeColor = System.Drawing.SystemColors.GrayText
        Me.txt_street.Location = New System.Drawing.Point(92, 60)
        Me.txt_street.Name = "txt_street"
        Me.txt_street.RightToLeft = System.Windows.Forms.RightToLeft.No
        Me.txt_street.Size = New System.Drawing.Size(238, 20)
        Me.txt_street.TabIndex = 28
        Me.txt_street.Text = "Street"
        '
        'txt_houseno
        '
        Me.txt_houseno.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend
        Me.txt_houseno.ForeColor = System.Drawing.SystemColors.GrayText
        Me.txt_houseno.Location = New System.Drawing.Point(92, 34)
        Me.txt_houseno.Name = "txt_houseno"
        Me.txt_houseno.RightToLeft = System.Windows.Forms.RightToLeft.No
        Me.txt_houseno.Size = New System.Drawing.Size(238, 20)
        Me.txt_houseno.TabIndex = 27
        Me.txt_houseno.Text = "House No."
        '
        'Button5
        '
        Me.Button5.Location = New System.Drawing.Point(160, 329)
        Me.Button5.Name = "Button5"
        Me.Button5.Size = New System.Drawing.Size(76, 26)
        Me.Button5.TabIndex = 5
        Me.Button5.Text = "Previous"
        Me.Button5.UseVisualStyleBackColor = True
        '
        'Button1
        '
        Me.Button1.Location = New System.Drawing.Point(324, 329)
        Me.Button1.Name = "Button1"
        Me.Button1.Size = New System.Drawing.Size(76, 26)
        Me.Button1.TabIndex = 4
        Me.Button1.Text = "Cancel"
        Me.Button1.UseVisualStyleBackColor = True
        '
        'btn_next_contact_info
        '
        Me.btn_next_contact_info.Enabled = False
        Me.btn_next_contact_info.Location = New System.Drawing.Point(242, 329)
        Me.btn_next_contact_info.Name = "btn_next_contact_info"
        Me.btn_next_contact_info.Size = New System.Drawing.Size(76, 26)
        Me.btn_next_contact_info.TabIndex = 3
        Me.btn_next_contact_info.Text = "Next"
        Me.btn_next_contact_info.UseVisualStyleBackColor = True
        '
        'TabPage3
        '
        Me.TabPage3.Controls.Add(Me.txt_confirmpword)
        Me.TabPage3.Controls.Add(Me.txt_pword)
        Me.TabPage3.Controls.Add(Me.txt_uname)
        Me.TabPage3.Controls.Add(Me.Button6)
        Me.TabPage3.Controls.Add(Me.Button7)
        Me.TabPage3.Controls.Add(Me.btn_finish)
        Me.TabPage3.Location = New System.Drawing.Point(4, 22)
        Me.TabPage3.Name = "TabPage3"
        Me.TabPage3.Padding = New System.Windows.Forms.Padding(3)
        Me.TabPage3.Size = New System.Drawing.Size(406, 361)
        Me.TabPage3.TabIndex = 2
        Me.TabPage3.Text = "Account Info"
        Me.TabPage3.UseVisualStyleBackColor = True
        '
        'txt_confirmpword
        '
        Me.txt_confirmpword.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend
        Me.txt_confirmpword.ForeColor = System.Drawing.SystemColors.GrayText
        Me.txt_confirmpword.Location = New System.Drawing.Point(96, 140)
        Me.txt_confirmpword.Name = "txt_confirmpword"
        Me.txt_confirmpword.RightToLeft = System.Windows.Forms.RightToLeft.No
        Me.txt_confirmpword.Size = New System.Drawing.Size(238, 20)
        Me.txt_confirmpword.TabIndex = 30
        Me.txt_confirmpword.Text = "Confirm Password"
        '
        'txt_pword
        '
        Me.txt_pword.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend
        Me.txt_pword.ForeColor = System.Drawing.SystemColors.GrayText
        Me.txt_pword.Location = New System.Drawing.Point(96, 101)
        Me.txt_pword.Name = "txt_pword"
        Me.txt_pword.RightToLeft = System.Windows.Forms.RightToLeft.No
        Me.txt_pword.Size = New System.Drawing.Size(238, 20)
        Me.txt_pword.TabIndex = 29
        Me.txt_pword.Text = "Password"
        '
        'txt_uname
        '
        Me.txt_uname.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend
        Me.txt_uname.ForeColor = System.Drawing.SystemColors.GrayText
        Me.txt_uname.Location = New System.Drawing.Point(96, 62)
        Me.txt_uname.Name = "txt_uname"
        Me.txt_uname.RightToLeft = System.Windows.Forms.RightToLeft.No
        Me.txt_uname.Size = New System.Drawing.Size(238, 20)
        Me.txt_uname.TabIndex = 28
        Me.txt_uname.Text = "User Name"
        '
        'Button6
        '
        Me.Button6.Location = New System.Drawing.Point(160, 329)
        Me.Button6.Name = "Button6"
        Me.Button6.Size = New System.Drawing.Size(76, 26)
        Me.Button6.TabIndex = 8
        Me.Button6.Text = "Previous"
        Me.Button6.UseVisualStyleBackColor = True
        '
        'Button7
        '
        Me.Button7.Location = New System.Drawing.Point(324, 329)
        Me.Button7.Name = "Button7"
        Me.Button7.Size = New System.Drawing.Size(76, 26)
        Me.Button7.TabIndex = 7
        Me.Button7.Text = "Cancel"
        Me.Button7.UseVisualStyleBackColor = True
        '
        'btn_finish
        '
        Me.btn_finish.Enabled = False
        Me.btn_finish.Location = New System.Drawing.Point(242, 329)
        Me.btn_finish.Name = "btn_finish"
        Me.btn_finish.Size = New System.Drawing.Size(76, 26)
        Me.btn_finish.TabIndex = 6
        Me.btn_finish.Text = "Finish"
        Me.btn_finish.UseVisualStyleBackColor = True
        '
        'New_Patient
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.ClientSize = New System.Drawing.Size(417, 386)
        Me.Controls.Add(Me.TabControl1)
        Me.Name = "New_Patient"
        Me.ShowIcon = False
        Me.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen
        Me.Text = "New Patient Entry"
        Me.TabControl1.ResumeLayout(False)
        Me.TabPage1.ResumeLayout(False)
        Me.TabPage1.PerformLayout()
        Me.GroupBox1.ResumeLayout(False)
        Me.GroupBox1.PerformLayout()
        Me.TabPage2.ResumeLayout(False)
        Me.TabPage2.PerformLayout()
        Me.TabPage3.ResumeLayout(False)
        Me.TabPage3.PerformLayout()
        Me.ResumeLayout(False)

    End Sub
    Friend WithEvents btn_next_geninfo As System.Windows.Forms.Button
    Friend WithEvents Button3 As System.Windows.Forms.Button
    Friend WithEvents TabControl1 As System.Windows.Forms.TabControl
    Friend WithEvents TabPage1 As System.Windows.Forms.TabPage
    Friend WithEvents TabPage2 As System.Windows.Forms.TabPage
    Friend WithEvents Button5 As System.Windows.Forms.Button
    Friend WithEvents Button1 As System.Windows.Forms.Button
    Friend WithEvents btn_next_contact_info As System.Windows.Forms.Button
    Friend WithEvents TabPage3 As System.Windows.Forms.TabPage
    Friend WithEvents Button6 As System.Windows.Forms.Button
    Friend WithEvents Button7 As System.Windows.Forms.Button
    Friend WithEvents btn_finish As System.Windows.Forms.Button
    Friend WithEvents Label15 As System.Windows.Forms.Label
    Friend WithEvents txt_weight As System.Windows.Forms.TextBox
    Friend WithEvents txt_height As System.Windows.Forms.TextBox
    Friend WithEvents txt_civil_status As System.Windows.Forms.TextBox
    Friend WithEvents GroupBox1 As System.Windows.Forms.GroupBox
    Friend WithEvents rdbtn_female As System.Windows.Forms.RadioButton
    Friend WithEvents rbtn_male As System.Windows.Forms.RadioButton
    Friend WithEvents birthdate_picker As System.Windows.Forms.DateTimePicker
    Friend WithEvents txt_occupation As System.Windows.Forms.TextBox
    Friend WithEvents txt_lname As System.Windows.Forms.TextBox
    Friend WithEvents txt_mname As System.Windows.Forms.TextBox
    Friend WithEvents txt_fname As System.Windows.Forms.TextBox
    Friend WithEvents txt_province As System.Windows.Forms.TextBox
    Friend WithEvents txt_city_municipality As System.Windows.Forms.TextBox
    Friend WithEvents txt_barangay As System.Windows.Forms.TextBox
    Friend WithEvents txt_street As System.Windows.Forms.TextBox
    Friend WithEvents txt_houseno As System.Windows.Forms.TextBox
    Friend WithEvents cmb_region As System.Windows.Forms.ComboBox
    Friend WithEvents txt_email As System.Windows.Forms.TextBox
    Friend WithEvents txt_telno As System.Windows.Forms.TextBox
    Friend WithEvents txt_mobileno As System.Windows.Forms.TextBox
    Friend WithEvents txt_zip As System.Windows.Forms.TextBox
    Friend WithEvents txt_confirmpword As System.Windows.Forms.TextBox
    Friend WithEvents txt_pword As System.Windows.Forms.TextBox
    Friend WithEvents txt_uname As System.Windows.Forms.TextBox
End Class
