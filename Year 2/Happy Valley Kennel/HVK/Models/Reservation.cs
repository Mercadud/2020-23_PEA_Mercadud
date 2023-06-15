using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using static HVK.Models.CustomerValidation;

#nullable disable

namespace HVK.Models
{
    public partial class Reservation
    {
        public Reservation()
        {
            PetReservations = new HashSet<PetReservation>();
            ReservationDiscounts = new HashSet<ReservationDiscount>();
        }

        public int ReservationId { get; set; }
        [Display(Name = "StartDate : ")]
        [DataType(DataType.Date)]
        [DisplayFormat(DataFormatString = "{0:yyyy-MM-dd}", ApplyFormatInEditMode = true)]
        [CompareToday]
        // [Required]
        public DateTime StartDate { get; set; }
        [Display(Name = "EndDate : ")]
        [DataType(DataType.Date)]
        [DisplayFormat(DataFormatString = "{0:yyyy-MM-dd}", ApplyFormatInEditMode = true)]
        // [Required]
        [CompareDates("StartDate")]
        public DateTime EndDate { get; set; }
        public decimal Status { get; set; }

        public virtual ICollection<PetReservation> PetReservations { get; set; }
        public virtual ICollection<ReservationDiscount> ReservationDiscounts { get; set; }
    }
}
