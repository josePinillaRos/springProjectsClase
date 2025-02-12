import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClienteService } from '../../services/cliente.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-cliente-form',
  templateUrl: './cliente-form.component.html',
  styleUrls: ['./cliente-form.component.css'],
})
export class ClienteFormComponent implements OnInit {
  clienteForm!: FormGroup;
  id!: number;
  isEditMode: boolean = false;

  constructor(
    private fb: FormBuilder,
    private clienteService: ClienteService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.clienteForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(12)]],
      apellido: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
    });

    this.id = this.route.snapshot.params['id'];
    if (this.id) {
      this.isEditMode = true;
      this.clienteService.getCliente(this.id).subscribe((cliente) => {
        this.clienteForm.patchValue(cliente);
      });
    }
  }

  guardarCliente(): void {
    if (this.clienteForm.invalid) {
      return;
    }

    const cliente = this.clienteForm.value;
    if (this.isEditMode) {
      this.clienteService.updateCliente(this.id, cliente).subscribe(() => {
        this.snackBar.open('Cliente actualizado con éxito', 'Cerrar', { duration: 3000 });
        this.router.navigate(['/clientes']);
      });
    } else {
      this.clienteService.createCliente(cliente).subscribe(() => {
        this.snackBar.open('Cliente creado con éxito', 'Cerrar', { duration: 3000 });
        this.router.navigate(['/clientes']);
      });
    }
  }
}
