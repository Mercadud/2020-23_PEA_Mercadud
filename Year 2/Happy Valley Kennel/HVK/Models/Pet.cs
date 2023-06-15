using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

#nullable disable

namespace HVK.Models
{
    public partial class Pet //: Customer
    {
        public enum Size { S, M, L };
        public enum Genders { M, F };
        public Pet()
        {
            PetReservations = new HashSet<PetReservation>();
            PetVaccinations = new HashSet<PetVaccination>();
        }

        public int PetId { get; set; }
        [Display(Name = "Name* :")]
        [DataType(DataType.Text)]
        [StringLength(25, ErrorMessage = "The name cannnot more than 25 characters")]
        [RegularExpression("^[a-zA-Z][A-Za-z-']*$", ErrorMessage = "Name can only contain letters , dash, apostrophe")]
        [Required]
        public string Name { get; set; }
        [Display(Name = "Gender* :")]
        [RegularExpression("(M|F)", ErrorMessage = "Invalid Selection")]
        [Required]
        public string Gender { get; set; }
        [Display(Name = "Breed :")]
        [DataType(DataType.Text)]
        [StringLength(50)]
        public string Breed { get; set; }
        [Display(Name = "Birth Year :")]
        [Range(2000,2022,ErrorMessage ="Valid years range from 2000 to 2022")]
        public int? Birthyear { get; set; }
        public int CustomerId { get; set; }
        [Display(Name = "Dog Size: ")]
        [RegularExpression("(S|M|L)", ErrorMessage = "Invalid Selection")]
        public string DogSize { get; set; }
        public bool Climber { get; set; }
        public bool Barker { get; set; }
        [Display(Name = "Special Notes:")]
        [DataType(DataType.MultilineText)]
        [StringLength(200, ErrorMessage = "No more than 200 characters")]
        public string SpecialNotes { get; set; }

        public virtual Customer Customer { get; set; }
        public virtual ICollection<PetReservation> PetReservations { get; set; }
        public virtual ICollection<PetVaccination> PetVaccinations { get; set; }
    }
}
