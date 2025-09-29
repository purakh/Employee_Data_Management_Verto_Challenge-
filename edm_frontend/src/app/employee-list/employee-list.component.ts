import { Component } from '@angular/core';
import { Employee } from '../employee';
import { EmployeeService } from '../employee.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.css']
})
export class EmployeeListComponent {

  employees: Employee[] = [];
  searchTerm: string = '';

  constructor(private employeeService: EmployeeService, private router: Router) {}

  ngOnInit(): void {
    this.getEmployees();
  }

  getEmployees() {
    this.employeeService.getEmployeesList().subscribe(data => {
      this.employees = data;
    });
  }

  updateEmployee(id: number) {
    this.router.navigate(['updating-by-id', id]);
  }

  deleteEmployee(id: number) {
    if (confirm("Are you sure to delete Employee ID: " + id)) {
      this.employeeService.deleteEmployee(id).subscribe(data => {
        console.log(data);
        this.getEmployees();
      });
    }
  }

  detailsOfEmployee(id: number) {
    this.router.navigate(['details-of-employee', id]);
  }

  // Earlier it was only search by ID now I updated it with search by name also
  
  searchEmployee() {
    if (this.searchTerm) {
      this.employeeService.getEmployeesList().subscribe(data => {
        this.employees = data.filter(emp => {
          const fullName = (emp.fname + ' ' + (emp.lname || '')).toLowerCase();
          return emp.id.toString().includes(this.searchTerm) ||
                 fullName.includes(this.searchTerm.toLowerCase());
        });
      });
    } else {
      this.getEmployees(); // reset if search is empty
    }
  }
}
