using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

#nullable disable

namespace HVK.Models
{
    public partial class Customer
    {
        public Customer()
        {
            Pets = new HashSet<Pet>();
        }

        public int CustomerId { get; set; }
        [Display(Name = "First Name:")]
        [DataType(DataType.Text)]
        //[Required(ErrorMessage = "You must enter a first name")]
        [RegularExpression("^[a-zA-Z]*[a-zA-Z '-]+$", ErrorMessage = "First Name must follow proper format")]
        [MaxLength(25, ErrorMessage = "First Name must be less than 25 characters")]
        public string FirstName { get; set; }
        [Display(Name = "Last Name:")]
        [DataType(DataType.Text)]
        //[Required(ErrorMessage = "You must enter a last name")]
        [RegularExpression("^[a-zA-Z]*[a-zA-Z '-]+$", ErrorMessage = "Last Name must follow proper format")]
        [MaxLength(25, ErrorMessage = "First Name must be less than 25 characters")]
        public string LastName { get; set; }
        // [Required]
        [StringLength(40, MinimumLength = 5, ErrorMessage = "Street needs 5 to 40 characters")]
        [Display(Name = "Street:")]
        public string Street { get; set; }
        [Display(Name = "City:")]
        [StringLength(25, ErrorMessage = "City max Length 25 characters")]
        public string City { get; set; }
        // [Required]
        [Display(Name = "Province:")]
        [StringLength(2)]
        public string Province { get; set; }
        // [Required]
        [Display(Name = "Postal Code:")]
        [StringLength(6)]
        public string PostalCode { get; set; }
        // [Required(ErrorMessage = "You must provide a cell phone number")]
        [Display(Name = "Home Phone #:")]
        [DataType(DataType.PhoneNumber)]
        [RegularExpression(@"^\(?([0-9]{3})\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$", ErrorMessage = "Not a valid phone number")]
        public string Phone { get; set; }
        // [Required(ErrorMessage = "You must provide a phone number")]
        [Display(Name = "Cell Phone #:")]
        [DataType(DataType.PhoneNumber)]
        [RegularExpression(@"^\(?([0-9]{3})\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$", ErrorMessage = "Not a valid phone number")]
        public string CellPhone { get; set; }
        [Display(Name = "Email:")]
        //[Required(ErrorMessage = "You must enter an email address")]
        [DataType(DataType.EmailAddress), MaxLength(50, ErrorMessage = "Email max Length 50 characters")]
        public string Email { get; set; }
        [Display(Name = "First Name:")]
        [DataType(DataType.Text)]
        [RegularExpression("^[a-zA-Z]*[a-zA-Z '-]+$", ErrorMessage = "First Name must follow proper format")]
        [MaxLength(25, ErrorMessage = "First Name must be less than 25 characters")]
        public string EmergencyContactFirstName { get; set; }
        [Display(Name = "Last Name:")]
        [DataType(DataType.Text)]
 
        [RegularExpression("^[a-zA-Z]*[a-zA-Z '-]+$", ErrorMessage = "Last Name must follow proper format")]
        [MaxLength(25, ErrorMessage = "Last Name must be less than 25 characters")]
        public string EmergencyContactLastName { get; set; }
        [Display(Name = "Emergency Phone #:")]
        public string EmergencyContactPhone { get; set; }
        // [Required(ErrorMessage = "Password is required.")]
        [Display(Name = "Password:")]
        [DataType(DataType.Text)]
        [MaxLength(50, ErrorMessage = "Password must be less than 50 characters")]
        public string Password { get; set; }

        public virtual ICollection<Pet> Pets { get; set; }
    }
}
