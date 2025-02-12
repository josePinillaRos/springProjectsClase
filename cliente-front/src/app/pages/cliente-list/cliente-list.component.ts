import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { ClienteService } from '../../services/cliente.service';
import { Cliente } from '../../models/cliente.model';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-cliente-list',
  templateUrl: './cliente-list.component.html',
  styleUrls: ['./cliente-list.component.css'],
})
export class ClienteListComponent implements OnInit {
  displayedColumns: string[] = ['id', 'nombre', 'apellido', 'email', 'acciones'];
  dataSource = new MatTableDataSource<Cliente>();
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  filtro: string = '';

  constructor(private clienteService: ClienteService, private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.cargarClientes();
  }

  cargarClientes(): void {
    this.clienteService.getClientes().subscribe((clientes) => {
      this.dataSource.data = clientes;
      this.dataSource.paginator = this.paginator;
    });
  }

  eliminarCliente(id: number): void {
    if (confirm('¿Está seguro de que desea eliminar este cliente?')) {
      this.clienteService.deleteCliente(id).subscribe(() => {
        this.snackBar.open('Cliente eliminado con éxito', 'Cerrar', { duration: 3000 });
        this.cargarClientes();
      });
    }
  }

  aplicarFiltro(): void {
    this.dataSource.filter = this.filtro.trim().toLowerCase();
  }
}
