import { DatePipe } from '@angular/common';

export class Employee {
    id!: number;
    fname!: string;
    lname!: string;;
    email!: string;;
    salary!: number;
    department: string;
    designation:string;
    joiningDate!: string;
  
    
   
  constructor() {
    
    this.fname="";
    this.lname="";
    this.email="";
    this.salary=0;
    this.department="";
    this.designation="";
    
}}
