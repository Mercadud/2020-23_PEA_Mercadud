using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using HVK.Models;
using Microsoft.AspNetCore.Http;


namespace HVK.Controllers
{
    public class CustomersController : Controller
    {
        private readonly HVK_MSSQL_W22Team4Context _context;

        public CustomersController(HVK_MSSQL_W22Team4Context context)
        {
            _context = context;
        }

        // GET: Customers
        public async Task<IActionResult> Index()
        {
            return UsefulFunctions.EzView(this, _context, id => {
                ViewBag.Reservations = _context.Reservations
                .OrderByDescending(r => r.StartDate).ToList();

                ViewBag.Pets = _context.Pets.Where(p => p.CustomerId == id).ToList();
            });
        }

        // GET: Customers/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            //it should be changed to be the current customer's id
            int userId = (id != null) ? (int)id : 2;
          
            ViewBag.Reservations = _context.Reservations
                .OrderByDescending(r => r.StartDate).ToList();

            ViewBag.Pets = _context.Pets.Where(p => p.CustomerId == userId).ToList();

            var result = _context.Customers.Where(c => c.CustomerId == userId).FirstOrDefault();

            return View(result);
        }

       

        // GET: Customers/Create
        public IActionResult Create()
        {
            return View();
        }

        // POST: Customers/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create(
            [Bind(
                "CustomerId,FirstName,LastName,Street,City,Province,PostalCode,Phone,CellPhone,Email,EmergencyContactFirstName,EmergencyContactLastName,EmergencyContactPhone,Password")]
            Customer customer)
        {
            if (ModelState.IsValid)
            {
                _context.Add(customer);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }

            return View(customer);
        }

        // GET: Customers/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var customer = await _context.Customers.FindAsync(id);
            if (customer == null)
            {
                return NotFound();
            }

            return View(customer);
        }

        // POST: Customers/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id,
            [Bind(
                "CustomerId,FirstName,LastName,Street,City,Province,PostalCode,Phone,CellPhone,Email,EmergencyContactFirstName,EmergencyContactLastName,EmergencyContactPhone,Password")]
            Customer customer)
        {
            if (id != customer.CustomerId)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(customer);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!CustomerExists(customer.CustomerId))
                    {
                        return NotFound();
                    }
                    else
                    {
                        throw;
                    }
                }

                return RedirectToAction(nameof(Index));
            }

            return View(customer);
        }

        // GET: Customers/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var customer = await _context.Customers
                .FirstOrDefaultAsync(m => m.CustomerId == id);
            if (customer == null)
            {
                return NotFound();
            }

            return View(customer);
        }

        // POST: Customers/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var customer = await _context.Customers.FindAsync(id);
            _context.Customers.Remove(customer);
            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool CustomerExists(int id)
        {
            return _context.Customers.Any(e => e.CustomerId == id);
        }
    }
}