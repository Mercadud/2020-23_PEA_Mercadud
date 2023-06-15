using System;
using System.Collections.Generic;

#nullable disable

namespace HVK.Models
{
    public partial class PetReservationService
    {
        public int PetReservationId { get; set; }
        public int ServiceId { get; set; }

        public virtual PetReservation PetReservation { get; set; }
        public virtual Service Service { get; set; }
    }
}
