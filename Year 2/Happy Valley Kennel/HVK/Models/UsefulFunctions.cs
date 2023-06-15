using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using HVK.Models;
using Microsoft.AspNetCore.Http;

namespace HVK.Models {
    public static class UsefulFunctions {
        public static IActionResult EzView(Controller c, HVK_MSSQL_W22Team4Context PassCust, Action<int> fun) {
            int CustId = c.HttpContext.Session.GetInt32("Id").Value;

            fun(CustId);
            if (PassCust == null) {
                return c.View();
            }
            else {
                Customer customer = PassCust.Customers.Where(c => c.CustomerId == CustId).FirstOrDefault();
                return c.View(customer);
            }
        }
        public static IActionResult EzView(Controller c, HVK_MSSQL_W22Team4Context PassCust) {
            int CustId = c.HttpContext.Session.GetInt32("Id").Value;

            if (PassCust == null) {
                return c.View();
            } else {
                Customer customer = PassCust.Customers.Where(c => c.CustomerId == CustId).FirstOrDefault();
                return c.View(customer);
            }
        }

        public static IActionResult EzView(Controller c, Customer cust, Action<int> fun) {
            int CustId = c.HttpContext.Session.GetInt32("Id").Value;

            fun(CustId);
            if (cust == null) {
                return c.View();
            } else {
                return c.View(cust);
            }
        }
        public static IActionResult EzView(Controller c, Customer cust) {
            if (cust == null) {
                return c.View();
            } else {
                return c.View(cust);
            }
        }
    }
}
