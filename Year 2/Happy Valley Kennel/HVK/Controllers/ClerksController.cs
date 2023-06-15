using HVK.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;

namespace HVK.Controllers
{
    public class ClerksController : Controller
    {
        
        private readonly HVK_MSSQL_W22Team4Context _context;

        public ClerksController(HVK_MSSQL_W22Team4Context context)
        {
            _context = context;
        }
        
        public IActionResult Index()
        {
            int userId = HttpContext.Session.GetInt32("Id").Value ;
          
            ViewBag.Clerk = _context.Customers.Where(c => c.CustomerId == userId).FirstOrDefault();
            var result = _context.Reservations.Include(r => r.PetReservations).ThenInclude(pr => pr.Pet).ThenInclude(p => p.Customer);

            return View(result.ToList());
        }

        
        public IActionResult Search(string search)
        {
            return UsefulFunctions.EzView(this, _context, id =>
            {
                if (search == null)
                    search = "";
                ViewBag.Customers =
                    _context.Customers
                        .Where(t => t.FirstName.Contains(search) || t.LastName.Contains(search));
            });
        }
    }
}
