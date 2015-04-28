Public Class New_Patient
    Private confirmed_pword As Boolean = False
    Private Sub btn_next_geninfo_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles btn_next_geninfo.Click
        TabControl1.SelectedIndex = 1
    End Sub

    Private Sub Button5_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Button5.Click
        TabControl1.SelectedIndex = 0
    End Sub

    Private Sub Button1_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Button1.Click
        Me.Close()
    End Sub

    Private Sub Button3_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Button3.Click
        Me.Close()
    End Sub

    Private Sub Button4_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles btn_next_contact_info.Click
        TabControl1.SelectedIndex = 2
    End Sub

    Private Sub Button6_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Button6.Click
        TabControl1.SelectedIndex = 1
    End Sub

    Private Sub New_Patient_FormClosing(ByVal sender As System.Object, ByVal e As System.Windows.Forms.FormClosingEventArgs) Handles MyBase.FormClosing
        Dim res As MsgBoxResult
        res = MsgBox("Are you sure you want to close this without saving", MsgBoxStyle.YesNo, "Warning!")
        If res = MsgBoxResult.Yes Then
            Me.Dispose()
        Else
            e.Cancel = True
        End If
    End Sub

    Private Sub Button7_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Button7.Click
        Me.Close()
    End Sub
#Region "Text Box hint effect on general info"
    Private Sub txt_fname_Enter(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_fname.Enter
        With txt_fname
            .Clear()
            .ForeColor = Color.Black
        End With
    End Sub

    Private Sub txt_mname_Enter(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_mname.Enter
        With txt_mname
            .Clear()
            .ForeColor = Color.Black
        End With
    End Sub

    Private Sub txt_lname_Enter(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_lname.Enter
        With txt_lname
            .Clear()
            .ForeColor = Color.Black
        End With
    End Sub

    Private Sub txt_occupation_Enter(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_occupation.Enter
        With txt_occupation
            .Clear()
            .ForeColor = Color.Black
        End With
    End Sub

    Private Sub txt_civil_status_Enter(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_civil_status.Enter
        With txt_civil_status
            .Clear()
            .ForeColor = Color.Black
        End With
    End Sub

    Private Sub txt_height_Enter(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_height.Enter
        With txt_height
            .Clear()
            .ForeColor = Color.Black
        End With
    End Sub

    Private Sub txt_weight_Enter(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_weight.Enter
        With txt_weight
            .Clear()
            .ForeColor = Color.Black
        End With
    End Sub

    Private Sub txt_fname_Leave(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_fname.Leave
        If txt_fname.Text = "" Then
            With txt_fname
                .Text = "First Name"
                .ForeColor = Color.Gray
            End With
        End If
    End Sub
    Private Sub txt_mname_Leave(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_mname.Leave
        If txt_mname.Text = "" Then
            With txt_mname
                .Text = "Middle Name"
                .ForeColor = Color.Gray
            End With
        End If
    End Sub

    Private Sub txt_lname_Leave(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_lname.Leave
        If txt_lname.Text = "" Then
            With txt_lname
                .Text = "Last Name"
                .ForeColor = Color.Gray
            End With
        End If
    End Sub

    Private Sub txt_occupation_Leave(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_occupation.Leave
        If txt_occupation.Text = "" Then
            With txt_occupation
                .Text = "Occupation"
                .ForeColor = Color.Gray
            End With
        End If
    End Sub

    Private Sub txt_civil_status_Leave(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_civil_status.Leave
        If txt_civil_status.Text = "" Then
            With txt_civil_status
                .Text = "Civil Status"
                .ForeColor = Color.Gray
            End With
        End If
    End Sub

    Private Sub txt_height_Leave(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_height.Leave
        If txt_height.Text = "" Then
            With txt_height
                .Text = "Height"
                .ForeColor = Color.Gray
            End With
        End If
    End Sub

    Private Sub txt_weight_Leave(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_weight.Leave
        If txt_weight.Text = "" Then
            With txt_weight
                .Text = "Weight"
                .ForeColor = Color.Gray
            End With
        End If
    End Sub
#End Region
#Region "Text Box hint effect on contact info"
    Private Sub txt_houseno_Enter(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_houseno.Enter
        With txt_houseno
            .Clear()
            .ForeColor = Color.Black
        End With
    End Sub

    Private Sub txt_street_Enter(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_street.Enter
        With txt_street
            .Clear()
            .ForeColor = Color.Black
        End With
    End Sub

    Private Sub txt_barangay_Enter(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_barangay.Enter
        With txt_barangay
            .Clear()
            .ForeColor = Color.Black
        End With
    End Sub

    Private Sub txt_city_municipality_Enter(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_city_municipality.Enter
        With txt_city_municipality
            .Clear()
            .ForeColor = Color.Black
        End With
    End Sub

    Private Sub txt_province_Enter(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_province.Enter
        With txt_province
            .Clear()
            .ForeColor = Color.Black
        End With
    End Sub

    Private Sub txt_zip_Enter(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_zip.Enter
        With txt_zip
            .Clear()
            .ForeColor = Color.Black
        End With
    End Sub

    Private Sub txt_mobileno_Enter(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_mobileno.Enter
        With txt_mobileno
            .Clear()
            .ForeColor = Color.Black
        End With
    End Sub

    Private Sub txt_telno_Enter(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_telno.Enter
        With txt_telno
            .Clear()
            .ForeColor = Color.Black
        End With
    End Sub

    Private Sub txt_email_Enter(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_email.Enter
        With txt_email
            .Clear()
            .ForeColor = Color.Black
        End With
    End Sub

    Private Sub txt_houseno_Leave(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_houseno.Leave
        If txt_houseno.Text = "" Then
            With txt_houseno
                .Text = "House No."
                .ForeColor = Color.Gray
            End With
        End If
    End Sub

    Private Sub txt_street_Leave(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_street.Leave
        If txt_street.Text = "" Then
            With txt_street
                .Text = "Street"
                .ForeColor = Color.Gray
            End With
        End If
    End Sub

    Private Sub txt_barangay_Leave(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_barangay.Leave
        If txt_barangay.Text = "" Then
            With txt_barangay
                .Text = "Barangay"
                .ForeColor = Color.Gray
            End With
        End If
    End Sub

    Private Sub txt_city_municipality_Leave(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_city_municipality.Leave
        If txt_city_municipality.Text = "" Then
            With txt_city_municipality
                .Text = "City/ Municipality"
                .ForeColor = Color.Gray
            End With
        End If
    End Sub

    Private Sub txt_province_Leave(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_province.Leave
        If txt_province.Text = "" Then
            With txt_province
                .Text = "Province"
                .ForeColor = Color.Gray
            End With
        End If
    End Sub
    Private Sub txt_zip_Leave(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_zip.Leave
        If txt_zip.Text = "" Then
            With txt_zip
                .Text = "Zip Code"
                .ForeColor = Color.Gray
            End With
        End If
    End Sub

    Private Sub txt_mobileno_Leave(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_mobileno.Leave
        If txt_mobileno.Text = "" Then
            With txt_mobileno
                .Text = "Mobile No."
                .ForeColor = Color.Gray
            End With
        End If
    End Sub

    Private Sub txt_telno_Leave(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_telno.Leave
        If txt_telno.Text = "" Then
            With txt_telno
                .Text = "Telephone No."
                .ForeColor = Color.Gray
            End With
        End If
    End Sub

    Private Sub txt_email_Leave(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_email.Leave
        If txt_email.Text = "" Then
            With txt_email
                .Text = "E-mail Address"
                .ForeColor = Color.Gray
            End With
        End If
    End Sub
#End Region
#Region "Text Box hint effect on account info"
    Private Sub txt_uname_Enter(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_uname.Enter
        With txt_uname
            .Clear()
            .ForeColor = Color.Black
        End With
    End Sub

    Private Sub txt_pword_Enter(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_pword.Enter
        With txt_pword
            .Clear()
            .ForeColor = Color.Black
            .UseSystemPasswordChar = True
        End With
    End Sub

    Private Sub txt_confirmpword_Enter(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_confirmpword.Enter
        With txt_confirmpword
            .Clear()
            .ForeColor = Color.Black
            .UseSystemPasswordChar = True
        End With
    End Sub

    Private Sub txt_uname_Leave(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_uname.Leave
        If txt_uname.Text = "" Then
            With txt_uname
                .Text = "User Name"
                .ForeColor = Color.Gray
            End With
        End If
    End Sub

    Private Sub txt_pword_Leave(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_pword.Leave
        If txt_pword.Text = "" Then
            With txt_pword
                .Text = "Password"
                .ForeColor = Color.Gray
                .UseSystemPasswordChar = False
            End With
        End If
    End Sub

    Private Sub txt_confirmpword_Leave(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_confirmpword.Leave
        If txt_confirmpword.Text = "" Then
            With txt_confirmpword
                .Text = "Confirm Password"
                .ForeColor = Color.Gray
                .UseSystemPasswordChar = False
            End With
        End If
    End Sub
#End Region

    Private Sub validate_gen_info()
        If txt_fname.Text = "First Name" Or txt_fname.Text = "" Or txt_lname.Text = "Last Name" Or txt_lname.Text = "" Or rdbtn_female.Checked = False And rbtn_male.Checked = False Or birthdate_picker.Value.Date = Date.Now().Date Then
            btn_next_geninfo.Enabled = False
        Else
            btn_next_geninfo.Enabled = True
        End If
    End Sub
    Private Sub validate_contact_info()
        If txt_houseno.Text = "House No." Or txt_houseno.Text = "" And txt_street.Text = "Street" Or txt_street.Text = "" And txt_barangay.Text = "Barangay" Or txt_barangay.Text = "" And txt_city_municipality.Text = "City/ Municipality" Or txt_city_municipality.Text = "" And txt_province.Text = "Province" Or txt_province.Text = "" Then
            btn_next_contact_info.Enabled = False
        Else
            btn_next_contact_info.Enabled = True
        End If
    End Sub
    Private Sub validate_account_info()
        If txt_uname.Text = "User Name" Or txt_uname.Text = "" Or txt_pword.Text = "Password" Or txt_pword.Text = "" Or txt_confirmpword.Text = "Confirm Password" Or txt_confirmpword.Text = "" Then
            btn_finish.Enabled = False
        Else
            If Not (txt_pword.Text = txt_confirmpword.Text) Then
                btn_finish.Enabled = True
            End If
        End If
    End Sub
    Private Sub txt_fname_TextChanged(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_fname.TextChanged
        If Not (txt_fname.Text = "First Name" Or txt_fname.Text = "") Then
            validate_gen_info()
        Else
            btn_next_geninfo.Enabled = False
        End If
    End Sub

    Private Sub txt_lname_TextChanged(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_lname.TextChanged
        If Not (txt_lname.Text = "Last Name" Or txt_lname.Text = "") Then
            validate_gen_info()
        Else
            btn_next_geninfo.Enabled = False
        End If
    End Sub

    Private Sub RadioButton1_CheckedChanged(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles rbtn_male.CheckedChanged
        validate_gen_info()
    End Sub

    Private Sub RadioButton2_CheckedChanged(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles rdbtn_female.CheckedChanged
        validate_gen_info()
    End Sub

    Private Sub New_Patient_Load(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles MyBase.Load
        birthdate_picker.MaxDate = Date.Now()
    End Sub
    Private Sub birthdate_picker_ValueChanged(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles birthdate_picker.ValueChanged
        validate_gen_info()
    End Sub

    Private Sub txt_houseno_TextChanged(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_houseno.TextChanged
        validate_contact_info()
    End Sub

    Private Sub txt_street_TextChanged(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_street.TextChanged
        validate_contact_info()
    End Sub

    Private Sub txt_barangay_TextChanged(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_barangay.TextChanged
        validate_contact_info()
    End Sub

    Private Sub txt_city_municipality_TextChanged(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_city_municipality.TextChanged
        validate_contact_info()
    End Sub

    Private Sub txt_province_TextChanged(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_province.TextChanged
        validate_contact_info()
    End Sub
    Private Sub txt_uname_TextChanged(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_uname.TextChanged
        validate_account_info()
    End Sub
    Private Sub txt_pword_TextChanged(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_pword.TextChanged
        validate_account_info()
    End Sub
    Private Sub txt_confirmpword_TextChanged(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txt_confirmpword.TextChanged
        validate_account_info()
    End Sub
    Private Sub btn_finish_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles btn_finish.Click
        If Not txt_confirmpword.Text = txt_pword.Text Then
            MsgBox("Password does not match", MsgBoxStyle.Information, "Account Information")
        End If
    End Sub
End Class